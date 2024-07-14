package com.github.alstinson.sportcronoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.alstinson.sportcronoapp.manager.ActivityManager;
import com.github.alstinson.sportcronoapp.manager.ButtonsManager;
import com.github.alstinson.sportcronoapp.manager.FrameManager;
import com.github.alstinson.sportcronoapp.manager.InputManager;
import com.github.alstinson.sportcronoapp.manager.MediaManager;
import com.github.alstinson.sportcronoapp.manager.CurrentValuesManager;
import com.github.alstinson.sportcronoapp.manager.StateManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public ButtonsManager buttonsManager;
    public FrameManager frameManager;
    public InputManager inputManager;
    public MediaManager mediaManager;
    public CurrentValuesManager currentValuesManager;
    public StateManager stateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        loadManagers();
        stateManager.setInit();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getFieldsValues(Class<T> superClass) {
        return getTransformedFieldsFromClass(superClass, field -> {
            try {
                return (T) field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to get field " + field.getName(), e);
            }
        });
    }

    private <T> List<T> getTransformedFieldsFromClass(Class<?> superClass, Function<Field, T> transformation) {
        return Arrays.stream(getClass().getFields())
                .filter(field -> superClass.isAssignableFrom(field.getType()))
                .map(transformation).
                collect(Collectors.toList());
    }

    private void loadManagers() {
        List<Field> fields =  getTransformedFieldsFromClass(ActivityManager.class, f -> f);
        for (Field field : fields) {
            try {
                Object fieldValue = field.getType()
                        .getConstructor(MainActivity.class)
                        .newInstance(this);
                field.set(this, fieldValue);
            } catch (Exception e) {
                throw new RuntimeException("Unable to setup field " + field.getName(), e);
            }
        }
    }
}