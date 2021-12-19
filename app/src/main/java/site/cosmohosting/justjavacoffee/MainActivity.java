package site.cosmohosting.justjavacoffee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Esta aplicación muestra un formulario para ordenar café
 * */
public class MainActivity extends AppCompatActivity {

    private int mQuantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Este método se llama cuando se presiona el botón ORDER
    * */
    public void submitOrder(View view){
        EditText correoElectronicoEditText = (EditText) findViewById(R.id.et_correoElectronico);
        String correoElectronico = correoElectronicoEditText.getText().toString();

        if (correoElectronico.isEmpty() || correoElectronico.equals("")){
            Toast.makeText(this,"Debe ingresar un correo electrónico",Toast.LENGTH_SHORT).show();
            return;
        }


        CheckBox cremaBatidaCheckBox = (CheckBox) findViewById(R.id.cb_cremaBatida);
        boolean tieneCremaBatida = cremaBatidaCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.cb_chocolate);
        boolean tieneChocolate = chocolateCheckBox.isChecked();

        EditText nombreEditText = (EditText) findViewById(R.id.et_nombre);
        String nombreCliente = nombreEditText.getText().toString();

        //Se utilizan 3 versiones del método calculatePrice
        int precio = calculatePrice(mQuantity, tieneChocolate, tieneCremaBatida);

        //int precio2 = calculatePrice(mQuantity, 5);
        //int precio3 = calculatePrice();

        //Imprime el mensaje
        String mensaje = createOrderSummary(precio, tieneCremaBatida, tieneChocolate, nombreCliente);
        displayMessage(mensaje);

        //Envía un correo electrónico
        composeEmail(correoElectronico,"JustJavaCoffe para " + nombreCliente, mensaje);
    }

    /**
     * Crea el resumen de la orden
     *
     * @param precio es el precio de la orden
     *
     * */
    private String createOrderSummary(int precio, boolean agregarCremaBatida, boolean agregarChocolate, String cliente){
        String resumenOrden = "";
        resumenOrden += "Nombre: " + cliente;
        resumenOrden += "\nCantidad: " + mQuantity + " cafés";

        resumenOrden += "\nQuiere crema batida?: " + agregarCremaBatida;
        resumenOrden += "\nQuiere chocolate?: " + agregarChocolate;

        resumenOrden += "\nTotal a pagar: $ " + precio;
        resumenOrden += "\nMuchas gracias!";

        return resumenOrden;
    }

    /**
     * Métodos para mostrar la información por pantalla
     * */
    private void displayQuantity(int cantidad){
        TextView quantityTextView = (TextView) findViewById(R.id.tv_quantity);
        quantityTextView.setText(""+cantidad);
    }

     private void displayMessage(String mensaje){
        TextView resumenOrdenTextView = (TextView) findViewById(R.id.tv_resumenOrden);
         resumenOrdenTextView.setText(mensaje);
     }

     /**
      * Métodos para controlar los botones de + y - cantidad
      *
      *
      * */
    public void increment(View view){
        if (mQuantity == 10) {
            Toast.makeText(this,"no puede pedir más de 10",Toast.LENGTH_LONG).show();
            return;
        }

        mQuantity += 1;
        displayQuantity(mQuantity);
    }

    public void decrement(View view){
        if (mQuantity ==1 ){
            Toast.makeText(this,"Pida algo... no se amarrado!",Toast.LENGTH_LONG).show();
            return;
        }

        mQuantity -= 1;
        displayQuantity(mQuantity);
    }

    /**
     * Calculates the price of the order.
     *
     * @param cantidad is the number of cups of coffee ordered
     * @return precio de la venta
     */
    private int calculatePrice(int cantidad, boolean tieneChocolate, boolean tieneCremaBatida) {
        int precioBase = 5;

        //Calcula el total de chocolate
        if (tieneChocolate){
            precioBase += 2;
        }

        //Calcula el total de la crema batida
        if (tieneCremaBatida){
            precioBase += 1;
        }

        return (cantidad * precioBase);
    }

    /**
     * Calculates the price of the order.
     * Asume un precio de $10
     *
     * @return precio de la venta
     */
    private int calculatePrice() {
        return (mQuantity * 10);
    }

    /**
     * Envía un correo electrónico
     *
     * @param addresses Direcciones de correo electrónico
     * @param mensaje Cuerpo del mensaje
     * @param subject Asunto del correo
     *
     * */
    public void composeEmail(String addresses, String subject, String mensaje) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}