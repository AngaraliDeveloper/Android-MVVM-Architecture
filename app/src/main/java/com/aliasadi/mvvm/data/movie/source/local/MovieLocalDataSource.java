package com.aliasadi.mvvm.data.movie.source.local;

import com.aliasadi.mvvm.data.movie.Movie;
import com.aliasadi.mvvm.data.movie.source.MovieDataSource;
import com.aliasadi.mvvm.data.movie.source.local.dao.MovieDao;
import com.aliasadi.mvvm.utils.DiskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Ali Asadi on 30/01/2019.
 */
public class MovieLocalDataSource implements MovieDataSource {

    private final Executor executor;
    private final MovieDao movieDao;

    private static MovieLocalDataSource instance;

    private MovieLocalDataSource(Executor executor, MovieDao movieDao) {
        this.executor = executor;
        this.movieDao = movieDao;
    }

    public static MovieLocalDataSource getInstance(MovieDao movieDao) {
        if (instance == null) {
            instance = new MovieLocalDataSource(new DiskExecutor(), movieDao);
        }
        return instance;
    }

    @Override
    public void getMovies(final LoadMoviesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Movie> movies = movieDao.getMovies();
                if (!movies.isEmpty()) {
                    callback.onMoviesLoaded(movies);
                } else {
                    callback.onDataNotAvailable();
                }
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void saveMovies(final List<Movie> movies) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                movieDao.saveMovies(movies);
            }
        };
        executor.execute(runnable);
    }
}
