package com.gpt.pulsarconsumer.real.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersistableUpdateDTO
{
    private LocalDateTime entryDate;

    private LocalDateTime updateDate;
}
