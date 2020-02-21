package com.example.calllogprovider;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {
    private Context context;
    private CursorAdapter cursorAdapter;

    public CallAdapter(Context context) {
        this.context = context;
        cursorAdapter = new CursorAdapter(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

            @Override
            /**
             * Método que crea un objeto View después de inflar la vista
             */
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_call, viewGroup, false);
                Holder holder = new Holder();
                holder.txvNumber = view.findViewById(R.id.txvNumber);
                holder.txvDate = view.findViewById(R.id.txvDate);
                holder.txvDuration = view.findViewById(R.id.txvDuration);
                holder.txvType = view.findViewById(R.id.txvType);
                view.setTag(holder);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                Holder holder = (Holder) view.getTag();
                holder.txvNumber.setText(cursor.getString(1));
                //Damos formato a la fecha
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String date = simpleDateFormat.format(new Date(Long.parseLong(cursor.getString(2))));
                holder.txvDate.setText(date);
                //holder.txvDuration.setText(String.valueOf(cursor.getString(3)));

                /**
                 * CONFIGURACIÓN DURACIÓN LLAMADA
                 */
                long duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(duration);

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);
                String callTime = (hour < 10 ? "0" + hour
                        : hour)
                        + " : "
                        + (minute < 10 ? "0" + minute : minute)
                        + " : "
                        + (seconds < 10 ? "0" + seconds
                        : seconds);
                holder.txvDuration.setText(callTime);
                /**
                 * TIPO LLAMADA
                 */
                String tipoLlamada = "Tipo Llamada: ";
                switch (cursor.getInt(4)) {
                    case CallLog
                            .Calls.INCOMING_TYPE:
                        tipoLlamada += "Entrante";
                        break;
                    case CallLog
                            .Calls.MISSED_TYPE:
                        tipoLlamada += "Perdida";
                        break;
                    case CallLog
                            .Calls.OUTGOING_TYPE:
                        tipoLlamada += "Saliente";
                        break;
                    default:
                        tipoLlamada += "Desconocido";
                        break;
                }
                holder.txvType.setText(tipoLlamada);
            }

            class Holder {
                TextView txvNumber;
                TextView txvDate;
                TextView txvDuration;
                TextView txvType;
            }
        };
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = cursorAdapter.newView(context, cursorAdapter.getCursor(), viewGroup);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder callViewHolder, int i) {
        cursorAdapter.getCursor().moveToPosition(i);
        cursorAdapter.bindView(callViewHolder.view, context, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    public void swap(Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }

    class CallViewHolder extends RecyclerView.ViewHolder {
        View view;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.itemview);


        }
    }
}
