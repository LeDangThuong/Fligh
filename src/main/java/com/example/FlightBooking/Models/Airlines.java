package com.example.FlightBooking.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Data
@Entity
@Table(name = "airlines")

/*Annotation @Table là một annotation JPA được sử dụng
để chỉ định tên bảng database mà một entity class liên kết đến.*/

public class Airlines {
    @Id  //Khóa chính
    @GeneratedValue (strategy = GenerationType.IDENTITY) // Sử dụng sequence hoặc auto-increment trong database để tự động tạo giá trị.
    // thường thì nó sẽ có giá trị 1 ++ lên dân
    private Long id;

}
