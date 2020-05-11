package org.ak.service;

import org.ak.dto.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.ak.config.JdbcConfig;
import org.ak.dao.JdbcTemplateDao;
import org.ak.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 *
 */
@Service
public class LocationExportService {
    //SQLs
    @Autowired
    private String selectLocationSql;

    @Autowired
    private String selectElevationSql;

    @Autowired
    private String selectLatitudeSql;

    @Autowired
    private String selectLongitudeSql;

    //DAOs
    @Autowired
    private JdbcTemplateDao<Location> locationDaoImpl;

    @Autowired
    private JdbcTemplateDao<Elevation> elevationDaoImpl;

    @Autowired
    private JdbcTemplateDao<Latitude> latitudeDaoImpl;

    @Autowired
    private JdbcTemplateDao<Longitude> longitudeDaoImpl;


    private static AtomicInteger locationCounter = new AtomicInteger(0);

    /**
     *
     * @return
     */
    public ParallelFlux<Location> parallelLocationFlux() {
        ParallelFlux<Location> locationFlux = Flux.fromStream(locationDaoImpl.findByQueryStream(selectLocationSql, null).filter(Objects::nonNull))
                .doOnSubscribe(s -> System.out.println("Starring Parallel: " + LocalTime.now()))
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::updateLocation)
                .doOnNext(p -> locationCounter.incrementAndGet())
                .doOnComplete(() -> System.out.println("The end parallel " + LocalTime.now() + " Locations processed : " + locationCounter.intValue()));
        return locationFlux;
    }

    /**
     *
     * @return
     */
    public Flux<Location> parallelSequentialLocationFlux() {
        Flux<Location> locationFlux = Flux.fromIterable(locationDaoImpl.findByQuery(selectLocationSql, null))
                .doOnSubscribe(s -> System.out.println("Starring parallel to sequential: " + LocalTime.now()))
                .subscribeOn(Schedulers.parallel())
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::updateLocation)
                .doOnNext(p -> locationCounter.incrementAndGet())
                .sequential()
                .doOnComplete(() -> System.out.println("The end parallel to sequential " + LocalTime.now() + " Locations processed : " + locationCounter.intValue()));
        ;
        return locationFlux;
    }

    /**
     *
     * @return
     */
    public Flux<Location> sequentialLocationFlux() {
        Flux<Location> locationFlux = Flux.defer(() -> Flux.fromIterable(locationDaoImpl.findByQuery(selectLocationSql, null)))
                .subscribeOn(Schedulers.elastic())
                .doOnSubscribe(s -> System.out.println("Starring sequential: " + LocalTime.now()))
                .map(this::updateLocation)
                .doOnNext(p -> locationCounter.incrementAndGet())
                .onErrorMap(RuntimeException::new)
                .doOnComplete(() -> System.out.println("The end sequential. " + LocalTime.now() + "Locations processed : " + locationCounter.intValue()));
        return locationFlux;
    }

    private <X> Mono<Location> updateLocationMono(List<X> data, Consumer<List<X>> setter) {
        Mono<Location> mono = Mono.defer(() -> Mono.fromRunnable(() -> {
            setter.accept(data);
        }));
        return mono;
    }

    private <X, Y, Z> Mono<Location> updateLocationMono(BiFunction<Y, Z, List<X>> dataPrivider, Y sql, Z parameters, Consumer<List<X>> setter) {
        Mono<Location> mono = Mono.defer(() -> Mono.fromRunnable(() -> {
            setter.accept(dataPrivider.apply(sql, parameters));
        }));
        return mono;
    }

    private Location updateLocation(Location location) {
        SqlParameterSource parameters = sqlParameters(location.getId());
        Flux<Location> updatedLocation = Flux.concat(
                updateLocationMono(elevationDaoImpl::findByQuery, selectElevationSql, parameters, location::withElevations)
                , updateLocationMono(latitudeDaoImpl::findByQuery, selectLatitudeSql, parameters, location::withLatitudes)
                , updateLocationMono(longitudeDaoImpl::findByQuery, selectLongitudeSql, parameters, location::withLongitude)
        );
        updatedLocation.subscribe();
        return location;
    }

    private SqlParameterSource sqlParameters(String id) {
        return new MapSqlParameterSource().addValue(JdbcConfig.ID, id);
    }
}