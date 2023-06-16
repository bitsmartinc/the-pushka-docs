package com.xc.payment.data;

import android.content.Context;

public class InjectorUtils {

    public static TransactionRepository getTransRepository(Context context) {
        return TransactionRepository.getInstance(AppDatabase.getInstance(context.getApplicationContext()).getTransactionDao());
    }
}
