package com.ToDoAPI.ToDo_APi.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
}
