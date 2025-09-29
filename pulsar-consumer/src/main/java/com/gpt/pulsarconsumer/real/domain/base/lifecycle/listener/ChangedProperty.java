package com.gpt.pulsarconsumer.real.domain.base.lifecycle.listener;


import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ChangedProperty
{
    @NotNull
    private String propertyName;
    private Object oldValue;
    private Object newValue;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ChangedProperty that = (ChangedProperty) o;
        return propertyName.equals(that.propertyName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(propertyName);
    }

    @Override
    public String toString()
    {
        return "ChangedProperty{" +
            "propertyName='" + propertyName + '\'' +
            ", oldValue=" + oldValue +
            ", newValue=" + newValue +
            '}';
    }
}

