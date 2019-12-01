package com.android.myannotations;

import android.os.Bundle;
import android.widget.EditText;

import com.android.myannotations.activitys.AddActivity;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class AddActivityTest {

    private AddActivity addActivity;

    @Before
    public void setup() {
        addActivity = Robolectric.buildActivity(AddActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void testFieldIsEmpty() {
        EditText edtTitulo = (EditText) addActivity.findViewById(R.id.edtTitulo);
        EditText edtMessage = (EditText) addActivity.findViewById(R.id.edtMessage);
        assertNotNull("EdtTitulo could not be found", edtTitulo);
        assertNotNull("EdtMessage could not be found", edtTitulo);
        edtTitulo.setText("Anotação 01!");
        edtMessage.setText("Aqui é a minha primeira anotação");

        assertThat("Campo título obrigatório não pode estar vazio","", not(equalTo(edtTitulo.getText().toString())));
        assertThat("Campo messagem obrigatório não pode estar vazio","", not(equalTo(edtMessage.getText().toString())));
    }
}
