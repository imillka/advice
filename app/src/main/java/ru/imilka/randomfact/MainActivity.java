package ru.imilka.randomfact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import ru.imilka.randomfact.fragment.FragmentAdvice;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private ImageButton info;
    private LinearLayout boxInfo, infoClose;
    public LinearLayout coockieLayout, beerLayout;


    BillingProcessor bp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = new BillingProcessor(this, getResources().getString(R.string.pub_key), this);
        bp.initialize();

        info = (ImageButton) findViewById(R.id.info);
        boxInfo = (LinearLayout) findViewById(R.id.info_box);
        infoClose = (LinearLayout) findViewById(R.id.info_close);

        coockieLayout = (LinearLayout) findViewById(R.id.coockieLayout);
        beerLayout = (LinearLayout) findViewById(R.id.beerLayout);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                new FragmentAdvice()).commit();


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boxInfo.setVisibility(View.VISIBLE);
                Animation animoutback = AnimationUtils.loadAnimation(MainActivity.this, R.anim.op_info);
                boxInfo.startAnimation(animoutback);

            }
        });

        infoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animoutback = AnimationUtils.loadAnimation(MainActivity.this, R.anim.cl_info);
                boxInfo.startAnimation(animoutback);

                animoutback.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        boxInfo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        coockieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.purchase(MainActivity.this, "coockie");
                bp.consumePurchase("coockie");
            }
        });

        beerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.purchase(MainActivity.this, "beer");
                bp.consumePurchase("beer");
            }
        });





    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(getBaseContext(), "Спасибо за покупку" + productId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getBaseContext(), "Ошибка покупки", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
