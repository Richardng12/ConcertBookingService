package se325.assignment01.concert.service.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import se325.assignment01.concert.common.types.Genre;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * This class represents a performer in the database, it contains the performer id, the name of the performer, the genre
 * of the performer, the blurb of the performer and finally a set of concerts that the performers perform in
 */
@Entity
@Table(name = "PERFORMERS")
public class Performer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "GENRE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @Column(name = "CONCERT", nullable = false)
    private Set<Concert> concerts;


    @Column(name = "BLURB", nullable = false, length = 1024)
    private String blurb;



    public Performer() {
    }


    public Performer(long performerID,String name, String imageName, Genre genre, String blurb) {
        this.id = performerID;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Set<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(Set<Concert> concerts) {
        this.concerts = concerts;
    }
}
