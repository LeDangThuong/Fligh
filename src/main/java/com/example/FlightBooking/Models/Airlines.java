package com.example.FlightBooking.Models;

import jakarta.persistence.*;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "airlines")

/*Annotation @Table là một annotation JPA được sử dụng
để chỉ định tên bảng database mà một entity class liên kết đến.*/

public class Airlines {
    @Id  //Khóa chính
    @GeneratedValue (strategy = GenerationType.IDENTITY) // Sử dụng sequence hoặc auto-increment trong database để tự động tạo giá trị.
    // thường thì nó sẽ có giá trị 1 ++ lên dân
    private Long id;
    private String airlineName;
    private String logoUrl;

    @OneToMany
    @JoinColumn(name = "plane_id")
    private Planes planes;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
