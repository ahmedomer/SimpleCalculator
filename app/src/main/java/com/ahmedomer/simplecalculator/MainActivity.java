package com.ahmedomer.simplecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private Stack<String> calculatorStack = new Stack<>();
    private boolean clearText = false;
    private boolean didChangeText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            showAboutScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void appendNumberOnScreen(String buttonTag)
    {
        didChangeText = true;

        double numberOnScreen = getNumberOnScreen();
        double numberPressed = 0;

        try
        {
            numberPressed = Double.parseDouble(buttonTag);
        }

        catch (NumberFormatException ex)
        {
            System.out.println("Exception: " + ex);
        }

        if (clearText)
        {
            setNumberOnScreen(numberPressed);
        }

        else
        {
            setNumberOnScreen((numberOnScreen * 10) + numberPressed);
        }
    }

    private void clearAll()
    {
        calculatorStack.clear();
        setNumberOnScreen(0);

        didChangeText = false;
        clearText = false;
    }

    public void didTapButton(View view)
    {
        TextView textView = (TextView) findViewById(R.id.textView);

        switch (view.getTag().toString())
        {
            case "plus":
            case "minus":
            case "multiply":
            case "divide":

                if (!didChangeText)
                {
                    if (!calculatorStack.isEmpty())
                    {
                        calculatorStack.pop();
                        calculatorStack.push(view.getTag().toString());
                    }
                }

                else
                {
                    resolveStack();
                    calculatorStack.push(textView.getText().toString());
                    calculatorStack.push(view.getTag().toString());
                }

                didChangeText = false;
                clearText = true;
                break;

            case "equals":
                resolveStack();
                clearText = true;
                break;

            case "percentage":
                percentage();
                clearText = true;
                break;

            case "invert":
                invert();
                clearText = false;
                break;

            case "clear":
                clearAll();
                break;

            default: // 0-9
                appendNumberOnScreen(view.getTag().toString());
                clearText = false;
        }
    }

    private double getNumberOnScreen()
    {
        TextView textView = (TextView) findViewById(R.id.textView);

        double number = 0;

        try
        {
            number = Double.parseDouble(textView.getText().toString());
        }

        catch (NumberFormatException ex)
        {
            System.out.println("Exception: " + ex);
        }

        return number;
    }

    private void invert()
    {
        double number = getNumberOnScreen();
        double invertedNumber = number * -1.0;

        setNumberOnScreen(invertedNumber);
    }

    private void percentage()
    {
        double number = getNumberOnScreen();
        double percentageNumber = number / 100.0;

        setNumberOnScreen(percentageNumber);
    }

    private void resolveStack()
    {
        if (calculatorStack.isEmpty())
        {
            return;
        }

        String operand = calculatorStack.pop();

        double firstNumber = Double.parseDouble(calculatorStack.pop());
        double secondNumber = getNumberOnScreen();
        double result = 0.0;

        switch (operand) {
            case "plus":
                result = firstNumber + secondNumber;
                break;

            case "minus":
                result = firstNumber - secondNumber;
                break;

            case "multiply":
                result = firstNumber * secondNumber;
                break;

            case "divide":
                result = firstNumber / secondNumber;
                break;

            default:
                break;
        }

        setNumberOnScreen(result);
    }

    private void setNumberOnScreen(double number)
    {
        TextView textView = (TextView) findViewById(R.id.textView);

        double fraction = number % 1;
        String textToDisplay;

        if (fraction != 0.0)
        {
            textToDisplay = Double.toString(number);
        }

        else
        {
            long wholeNumber = (long)number;
            textToDisplay = Long.toString(wholeNumber);
        }

        textView.setText(textToDisplay);
    }

    private void showAboutScreen()
    {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}