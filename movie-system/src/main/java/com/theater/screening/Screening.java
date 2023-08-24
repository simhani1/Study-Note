package com.theater.screening;

import com.theater.movie.Movie;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Screening {

    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;
}
