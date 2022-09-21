package com.masterweb.firdausapps.clasess;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.masterweb.firdausapps.azan.PrayerTimes;
import com.masterweb.firdausapps.azan.TimeAdjustment;
import com.masterweb.firdausapps.azan.TimeCalculator;
import com.masterweb.firdausapps.azan.types.AngleCalculationType;
import com.masterweb.firdausapps.azan.types.BaseTimeAdjustmentType;
import com.masterweb.firdausapps.azan.types.PrayersType;
import com.masterweb.firdausapps.service.GPSTracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MasterFunction {
    Calendar calendar;
    String formattglarab;
    int manual_hijriyyah = 2;
    PrayerTimes lokal_prayerTimes;
    PrayerTimes prayerTimes;
    int thnarab;
    String alarmadzanskg;
    Locale localeIndonesia = new Locale("id", "ID");
    public static final String[] nama_hari_arab_ID = new String[]{
            "Al-Ahad",
            "Al-Itsnain",
            "Ats-Tsulaatsaa'",
            "Al-Arbi'a'",
            "Al-Khamiis",
            "Al-Jumu'ah",
            "As-Sabt"};

    SimpleDateFormat formatterid_hari = new SimpleDateFormat("EEEE");
    SimpleDateFormat formatterid = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat sdfjam = new SimpleDateFormat("HH:mm");
    Context mContext;
    GPSTracker gpsTracker;
    TimeAdjustment adjustments;
    boolean hanafi_asr_ratio = false;
    GregorianCalendar date;
    TimeCalculator timecalc;
    public MasterFunction(Context context){
        this.mContext = context;
        gpsTracker = new GPSTracker(mContext);
        adjustments = new TimeAdjustment(BaseTimeAdjustmentType.TWO_MINUTES_ADJUSTMENT);

        timecalc = new TimeCalculator();
        date = new GregorianCalendar();
        timecalc.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH, hanafi_asr_ratio, adjustments);
        lokal_prayerTimes = timecalc
                .date(date)
                .dateRelative(0)
                .location(gpsTracker.getLatitude(),
                        gpsTracker.getLongitude(),
                        gpsTracker.getAltitude(), 0)
                .calculateTimes();
        lokal_prayerTimes.setUseSecond(false);
    }
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }
    public String device(){
        String s = "";
        s += android.os.Build.VERSION.RELEASE;
        s += android.os.Build.BRAND;
        s += android.os.Build.HARDWARE;
        s += android.os.Build.ID;
        return s.replace(".","");
    }
    public String tglIndo(){
        calendar = Calendar.getInstance();
        SimpleDateFormat formatIncoming = new SimpleDateFormat("EEEE, dd MMMM yyyy", localeIndonesia);
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        formatIncoming.setTimeZone(tz);
        return formatIncoming.format(calendar.getTime());
    }
    public String getTimes(){
        calendar = Calendar.getInstance();
        SimpleDateFormat formatIncoming = new SimpleDateFormat("yyyy-MM-dd", localeIndonesia);
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        formatIncoming.setTimeZone(tz);
        return formatIncoming.format(calendar.getTime());
    }
    public String tglHijriyah(){
        calendar = Calendar.getInstance();
        String hr_ini="";
        GregorianCalendar gCal = new GregorianCalendar();
        Calendar uCal = new UmmalquraCalendar();
        uCal.setTime(gCal.getTime());
        uCal.add(Calendar.DAY_OF_MONTH, manual_hijriyyah - 2);
        thnarab = uCal.get(Calendar.YEAR);         // 1433
        String blnarab = uCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);        // 2
        int tglarab = uCal.get(Calendar.DAY_OF_MONTH); // 20d
        int harihijriyah = gCal.get(Calendar.DAY_OF_WEEK) - 1;
        hr_ini = nama_hari_arab_ID[harihijriyah];
        formattglarab = hr_ini+", "+pad(tglarab) + " " + blnarab + " " + thnarab + "H"; // Tuesday 8 Rabi' al-Awwal, 1433
        return formattglarab;
    }
    public String time_subuh(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.FAJR));
    }
    public String time_fajar(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.SUNRISE));
    }
    public String time_dzuhur(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ZUHR));
    }
    public String time_ashar(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ASR));
    }
    public String time_maghrib(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.MAGHRIB));
    }
    public String time_isya(){
        return sdfjam.format(lokal_prayerTimes.getPrayTime(PrayersType.ISHA));
    }
    private void calculate_adzan(GregorianCalendar date) {
        TimeAdjustment adjustments;
        adjustments = new TimeAdjustment(BaseTimeAdjustmentType.TWO_MINUTES_ADJUSTMENT);
        TimeCalculator timecalc = new TimeCalculator();
        timecalc.timeCalculationMethod(AngleCalculationType.MUHAMMADIYAH, hanafi_asr_ratio, adjustments);
        prayerTimes = timecalc
                .date(date)
                .dateRelative(0)
                .location(gpsTracker.getLatitude(),
                        gpsTracker.getLongitude(),
                        gpsTracker.getAltitude(), 0)
                .calculateTimes();
        prayerTimes.setUseSecond(false);
    }
    Calendar waktu_alarm_last;
    Calendar waktu_alarm;
    Calendar waktu_now;
    int waktushalat;
    String waktuadzanskg;
    String waktuadzannextshort;
    public void getNextTime(TextView next,TextView next_time,TextView selisih,TextView timer){
        SimpleDateFormat sdfjam = new SimpleDateFormat("HH:mm");
        //ambil jadwal isya hari kemarin
        waktu_alarm_last = Calendar.getInstance();
        waktu_alarm_last.add(Calendar.DAY_OF_MONTH, -1);
        calculate_adzan(new GregorianCalendar(waktu_alarm_last.get(Calendar.YEAR), waktu_alarm_last.get(Calendar.MONTH), waktu_alarm_last.get(Calendar.DAY_OF_MONTH), waktu_alarm_last.get(Calendar.HOUR_OF_DAY), waktu_alarm_last.get(Calendar.MINUTE), waktu_alarm_last.get(Calendar.SECOND)));
        waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.ISHA));
        waktu_alarm_last.set(Calendar.SECOND, 0);
        waktu_alarm_last.set(Calendar.MILLISECOND, 0);
        alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.ISHA));

        //ambil jadwal subuh hari ini
        calculate_adzan(new GregorianCalendar());
        waktu_alarm = Calendar.getInstance();
        waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.FAJR));
        waktu_alarm.set(Calendar.SECOND, 0);
        waktu_alarm.set(Calendar.MILLISECOND, 0);
        waktu_now = Calendar.getInstance();
        waktu_now.set(Calendar.SECOND, 20); //biar beda dgn alarm, klo kbetulan detik sama (0) bs error ->  if (waktu_alarm.before(waktu_now)) {
        waktu_now.set(Calendar.MILLISECOND, 0);

        waktushalat = 5;
        waktuadzanskg = "Waktu I'sya";
        waktuadzannextshort = "Waktu Subuh";
        next.setText(waktuadzannextshort);
        next_time.setText(sdfjam.format(prayerTimes.getPrayTime(PrayersType.FAJR)));
        covertTimeToText(selisih,timer,sdfjam.format(prayerTimes.getPrayTime(PrayersType.FAJR)),"\nMenjelang Waktu Subuh");
        if (waktu_alarm.before(waktu_now)) {
            waktushalat = 1;
            waktuadzanskg = "Waktu Subuh";
            waktuadzannextshort = "Waktu Syuruq";
            alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.FAJR));
            waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.SUNRISE));
            waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.FAJR));
            waktu_alarm.set(Calendar.SECOND, 0);
            waktu_alarm.set(Calendar.MILLISECOND, 0);
            waktu_alarm_last.set(Calendar.SECOND, 0);
            waktu_alarm_last.set(Calendar.MILLISECOND, 0);
            next.setText(waktuadzannextshort);
            next_time.setText(sdfjam.format(prayerTimes.getPrayTime(PrayersType.SUNRISE)));
            covertTimeToText(selisih,timer,sdfjam.format(prayerTimes.getPrayTime(PrayersType.SUNRISE)),"\nMenjelang Waktu Syuruq");
            if (waktu_alarm.before(waktu_now)) {
                waktushalat = 2;
                waktuadzanskg = "Waktu Dzuhur";
                waktuadzannextshort = "Waktu Ashar";
                alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.ZUHR));

                waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.ASR));
                waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.ZUHR));
                waktu_alarm.set(Calendar.SECOND, 0);
                waktu_alarm.set(Calendar.MILLISECOND, 0);
                waktu_alarm_last.set(Calendar.SECOND, 0);
                waktu_alarm_last.set(Calendar.MILLISECOND, 0);
                next.setText(waktuadzanskg);
                next_time.setText(sdfjam.format(prayerTimes.getPrayTime(PrayersType.ZUHR)));
                covertTimeToText(selisih,timer,sdfjam.format(prayerTimes.getPrayTime(PrayersType.ZUHR)),"\nMenjelang Waktu Dzuhur");
                if (waktu_alarm.before(waktu_now)) {
                    waktushalat = 3;
                    waktuadzanskg = "Waktu Ashar";
                    waktuadzannextshort = "Waktu Maghrib";
                    alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.ASR));

                    waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.MAGHRIB));
                    waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.ASR));
                    waktu_alarm.set(Calendar.SECOND, 0);
                    waktu_alarm.set(Calendar.MILLISECOND, 0);
                    waktu_alarm_last.set(Calendar.SECOND, 0);
                    waktu_alarm_last.set(Calendar.MILLISECOND, 0);
                    next.setText(waktuadzannextshort);
                    next_time.setText(sdfjam.format(prayerTimes.getPrayTime(PrayersType.MAGHRIB)));
                    covertTimeToText(selisih,timer,sdfjam.format(prayerTimes.getPrayTime(PrayersType.MAGHRIB)),"\nMenjelang Waktu Maghrib");
                    if (waktu_alarm.before(waktu_now)) {
                        waktushalat = 4;
                        waktuadzanskg = "Waktu Maghrib";
                        waktuadzannextshort = "Waktu I'sya";
                        alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.MAGHRIB));

                        waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.ISHA));
                        waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.MAGHRIB));
                        waktu_alarm.set(Calendar.SECOND, 0);
                        waktu_alarm.set(Calendar.MILLISECOND, 0);
                        waktu_alarm_last.set(Calendar.SECOND, 0);
                        waktu_alarm_last.set(Calendar.MILLISECOND, 0);
                        next.setText(waktuadzannextshort);
                        next_time.setText(sdfjam.format(prayerTimes.getPrayTime(PrayersType.ISHA)));
                        covertTimeToText(selisih,timer,sdfjam.format(prayerTimes.getPrayTime(PrayersType.ISHA)),"\nMenjelang Waktu Isya");
                        if (waktu_alarm.before(waktu_now)) {
                            waktushalat = 5;
                            waktuadzanskg = "Waktu Maghrib";
                            waktuadzannextshort = "Tengah Malam";
                            alarmadzanskg = sdfjam.format(prayerTimes.getPrayTime(PrayersType.MAGHRIB));

                            waktu_alarm.setTime(prayerTimes.getPrayTime(PrayersType.ISHA));
                            waktu_alarm_last.setTime(prayerTimes.getPrayTime(PrayersType.MAGHRIB));
                            waktu_alarm.set(Calendar.SECOND, 0);
                            waktu_alarm.set(Calendar.MILLISECOND, 0);
                            waktu_alarm_last.set(Calendar.SECOND, 0);
                            waktu_alarm_last.set(Calendar.MILLISECOND, 0);
                            next.setText(waktuadzannextshort);
                            next_time.setText("24:00");
                            covertTimeToText(selisih,timer,"24:59","\nMenjelang Tengah Malam");
                        }
                    }
                }
            }
        }
    }
    public String covertTimeToText(TextView selisih,TextView timer,String dataDate,String ket) {
        String waktu = getTimes()+" "+dataDate+":00";
        String convTime = null;

        String prefix = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date pasTime = dateFormat.parse(waktu);
            Date nowTime = new Date();
            new CountDownTimer(pasTime.getTime()-nowTime.getTime() , 1000) {

                public void onTick(long millisUntilFinished) {
                    long diff = millisUntilFinished;
                    long Days = diff / (24 * 60 * 60 * 1000);
                    long Hours = diff / (60 * 60 * 1000) % 24;
                    long Minutes = diff / (60 * 1000) % 60;
                    long Seconds = diff / 1000 % 60;
                    timer.setText(String.format("%02d", Hours)+":"+String.format("%02d", Minutes)+":"+String.format("%02d", Seconds));
                    selisih.setText(String.format("%02d", Hours)+" Jam "+String.format("%02d", Minutes)+" Menit "+ket);
                }

                public void onFinish() {
                    // TODO Auto-generated method stub
                }
            }.start();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return waktu;
    }
    public String huruf_arab(String val) {
        char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<val.length();i++){
            if(Character.isDigit(val.charAt(i))){
                builder.append(arabicChars[(int)(val.charAt(i))-48]);
            }
            else{
                builder.append(val.charAt(i));
            }
        }
        return builder.toString();
    }
}
