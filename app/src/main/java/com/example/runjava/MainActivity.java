package com.example.runjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *
     * This method is called when minus button is clicked
     */
    public void decrease(View view) {
        // preventing quantity to be less than 1
        if (quantity==1) {
            showToast(getString(R.string.lowest_quantity_limit_message));
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }

    /**
     *
     * This method is called when plus button is clicked
     */
    public void increase(View view) {
        // preventing quantity to be more than 100
        if (quantity==100) {
            showToast(getString(R.string.highest_quantity_limit_message));
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    /**
     *
     * This method is called when order button is clicked
     */
    public void submitOrder(View view) {
        // Extracting name of the user from name field
        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        // customer wants whipped cream or not
        CheckBox whippedCreamCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean addWhippedCream = whippedCreamCreamCheckBox.isChecked();

        // customer wants chocolate or not
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean addChocolate = chocolateCheckBox.isChecked();

        // calculating total price
        int price = calculatePrice(quantity, addWhippedCream, addChocolate);
        String orderSummaryMessage = createOrderSummaryMessage(name, addWhippedCream, quantity, addChocolate, price);

        // Launching gmail to sent order message
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_subject,name));
        intent.putExtra(Intent.EXTRA_TEXT, orderSummaryMessage);

        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else
            showToast(getString(R.string.unhandled_intent_message));
    }

    /**
     *
     * @param quantity quantity ordered
     * @param addWhippedCream customer wants whipped cream or not
     * @param addChocolate customer wants chocolate or not
     * @return total price of the order
     */
    private int calculatePrice(int quantity, boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;
        if (addWhippedCream)
            basePrice++;
        if (addChocolate)
            basePrice += 2;
        return quantity * basePrice;
    }

    /**
     *
     * @param name name of the customer
     * @param addWhippedCream customer wants whipped cream or not
     * @param quantity quantity ordered
     * @param addChocolate customer wants chocolate or not
     * @param price total price of the order
     * @return order message
     */
    private String createOrderSummaryMessage(String name, boolean addWhippedCream, int quantity, boolean addChocolate, int price) {
        String orderSummary = getString(R.string.customer_name,name);
        orderSummary += getString(R.string.add_whipped_cream) + addWhippedCream;
        orderSummary += getString(R.string.add_chocolate) + addChocolate;
        orderSummary += getString(R.string.total_quantity) + quantity;
        orderSummary += getString(R.string.total) + NumberFormat.getCurrencyInstance().format(price);
        orderSummary += getString(R.string.thank_you_message);

        return orderSummary;
    }

    /**
     *
     * @param quantity current quantity to be displayed
     *  This Method displays the current quantity in quantity TextView
     */
    private void displayQuantity(int quantity) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(quantity));
    }

    /**
     *
     * @param message message to be displayed in toast
     * Displays a toast on the screen
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}