package de.munich.iteratec.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class Tab1Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Tab1Fragment";

    private DatabaseReference database;
    private WebView iconsWebview;
    private AlertDialog.Builder webviewDialogBuilder;
    private AlertDialog webviewDialog;
    public static ArrayList<Question> questions;
    private ViewFlipper viewflipperEditor, viewflipperPreview;
    private Spinner spinner;
    private EditText questionEdit, answer1Text, answer2Text, answer3Text, answer4Text;
    private TextView answer1Image, answer2Image, answer3Image, answer4Image;
    private EditText imageDescription1, imageDescription2, imageDescription3, imageDescription4;
    private ImageView descriptionImage1, descriptionImage2, descriptionImage3, descriptionImage4;
    private ImageView answer1ImagePreview, answer2ImagePreview, answer3ImagePreview, answer4ImagePreview;
    private TextView questionPreview, answer1TextPreview, answer2TextPreview, answer3TextPreview, answer4TextPreview;
    private Button textQuestionAdd, imageQuestionAdd, ratingQuestionAdd;
    private SmileRating rating;
    private EditText smileyRatingText1, smileyRatingText2, smileyRatingText3, smileyRatingText4, smileyRatingText5;
    private EditText textDateBegin, textDateEnd, imageDateBegin, imageDateEnd, ratingDateBegin, ratingDateEnd;
    private long dateMilliSecBegin, dateMilliSecEnd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab1,container,false);

        //initiate views etc.
        questions = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();

        viewflipperEditor = view.findViewById(R.id.viewflipperEditor);
        viewflipperPreview = view.findViewById(R.id.viewflipperPreview);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        questionEdit = (EditText) view.findViewById(R.id.questionEdit);

        answer1Text = (EditText) view.findViewById(R.id.answer1Text);
        answer2Text = (EditText) view.findViewById(R.id.answer2Text);
        answer3Text = (EditText) view.findViewById(R.id.answer3Text);
        answer4Text = (EditText) view.findViewById(R.id.answer4Text);

        answer1Image = (TextView) view.findViewById(R.id.answer1Image);
        answer2Image = (TextView) view.findViewById(R.id.answer2Image);
        answer3Image = (TextView) view.findViewById(R.id.answer3Image);
        answer4Image = (TextView) view.findViewById(R.id.answer4Image);

        imageDescription1 = (EditText) view.findViewById(R.id.description1);
        imageDescription2 = (EditText) view.findViewById(R.id.description2);
        imageDescription3 = (EditText) view.findViewById(R.id.description3);
        imageDescription4 = (EditText) view.findViewById(R.id.description4);

        descriptionImage1 = (ImageView) view.findViewById(R.id.descriptionImage1);
        descriptionImage2 = (ImageView) view.findViewById(R.id.descriptionImage2);
        descriptionImage3 = (ImageView) view.findViewById(R.id.descriptionImage3);
        descriptionImage4 = (ImageView) view.findViewById(R.id.descriptionImage4);


        rating = (SmileRating) view.findViewById(R.id.smile_rating);
        smileyRatingText1 = (EditText) view.findViewById(R.id.ratingText1);
        smileyRatingText2 = (EditText) view.findViewById(R.id.ratingText2);
        smileyRatingText3 = (EditText) view.findViewById(R.id.ratingText3);
        smileyRatingText4 = (EditText) view.findViewById(R.id.ratingText4);
        smileyRatingText5 = (EditText) view.findViewById(R.id.ratingText5);

        questionPreview = (TextView) view.findViewById(R.id.questionPreview);

        answer1TextPreview = (TextView) view.findViewById(R.id.answer1TextPreview);
        answer2TextPreview = (TextView) view.findViewById(R.id.answer2TextPreview);
        answer3TextPreview = (TextView) view.findViewById(R.id.answer3TextPreview);
        answer4TextPreview = (TextView) view.findViewById(R.id.answer4TextPreview);

        answer1ImagePreview = (ImageView) view.findViewById(R.id.answer1ImagePreview);
        answer2ImagePreview = (ImageView) view.findViewById(R.id.answer2ImagePreview);
        answer3ImagePreview = (ImageView) view.findViewById(R.id.answer3ImagePreview);
        answer4ImagePreview = (ImageView) view.findViewById(R.id.answer4ImagePreview);

        textDateBegin = (EditText) view.findViewById(R.id.textDateBegin);
        textDateEnd = (EditText) view.findViewById(R.id.textDateEnd);
        imageDateBegin = (EditText) view.findViewById(R.id.imageDateBegin);
        imageDateEnd = (EditText) view.findViewById(R.id.imageDateEnd);
        ratingDateBegin = (EditText) view.findViewById(R.id.ratingDateBegin);
        ratingDateEnd = (EditText) view.findViewById(R.id.ratingDateEnd);

        textQuestionAdd = (Button) view.findViewById(R.id.addTextQuestionBtn);
        imageQuestionAdd = (Button) view.findViewById(R.id.addImageQuestionBtn);
        ratingQuestionAdd = (Button) view.findViewById(R.id.addRatingQuestionBtn);


        //create ArrayAdapter<String>
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.layouts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set ArrayAdapter on Spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //add TextWatcher to change text in TextView dynamically
        questionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    questionPreview.setText(questionEdit.getText().toString());
                } else {
                    questionPreview.setHint("Frage");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        answer1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    answer1TextPreview.setText(answer1Text.getText().toString());
                } else {
                    answer1TextPreview.setHint("Antwort 1");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        answer2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    answer2TextPreview.setText(answer2Text.getText().toString());
                } else {
                    answer2TextPreview.setHint("Antwort 2");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        answer3Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    answer3TextPreview.setText(answer3Text.getText().toString());
                }else {
                    answer3TextPreview.setHint("Antwort 3");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        answer4Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    answer4TextPreview.setText(answer4Text.getText().toString());
                }else {
                    answer4TextPreview.setHint("Antwort 4");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        answer1ImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupWebview();
                setupAlertDialogWithWebview();
                iconsWebview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        WebView view = (WebView) v;
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                            Log.i("Result", hitTestResult.getExtra().toString());
                            String url = hitTestResult.getExtra().toString();
                            answer1Image.setText(url);
                            SvgLoader.pluck().with(getActivity()).load(url, answer1ImagePreview);
                            SvgLoader.pluck().with(getActivity()).load(url, descriptionImage1);
                            webviewDialog.dismiss();
                        }

                        return true;
                    }
                });
            }
        });

        answer2ImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupWebview();
                setupAlertDialogWithWebview();
                iconsWebview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        WebView view = (WebView) v;
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                            Log.i("Result", hitTestResult.getExtra().toString());
                            String url = hitTestResult.getExtra().toString();
                            answer2Image.setText(url);
                            SvgLoader.pluck().with(getActivity()).load(url, answer2ImagePreview);
                            SvgLoader.pluck().with(getActivity()).load(url, descriptionImage2);
                            webviewDialog.dismiss();
                        }

                        return true;
                    }
                });
            }
        });

        answer3ImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupWebview();
                setupAlertDialogWithWebview();
                iconsWebview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        WebView view = (WebView) v;
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                            Log.i("Result", hitTestResult.getExtra().toString());
                            String url = hitTestResult.getExtra().toString();
                            answer3Image.setText(url);
                            SvgLoader.pluck().with(getActivity()).load(url, answer3ImagePreview);
                            SvgLoader.pluck().with(getActivity()).load(url, descriptionImage3);
                            webviewDialog.dismiss();
                        }

                        return true;
                    }
                });
            }
        });

        answer4ImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupWebview();
                setupAlertDialogWithWebview();
                iconsWebview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        WebView view = (WebView) v;
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                            Log.i("Result", hitTestResult.getExtra().toString());
                            String url = hitTestResult.getExtra().toString();
                            answer4Image.setText(url);
                            SvgLoader.pluck().with(getActivity()).load(url, answer4ImagePreview);
                            SvgLoader.pluck().with(getActivity()).load(url, descriptionImage4);
                            webviewDialog.dismiss();
                        }

                        return true;
                    }
                });
            }
        });


        smileyRatingText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    rating.setNameForSmile(BaseRating.TERRIBLE, smileyRatingText1.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        smileyRatingText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    rating.setNameForSmile(BaseRating.BAD, smileyRatingText2.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        smileyRatingText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    rating.setNameForSmile(BaseRating.OKAY, smileyRatingText3.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        smileyRatingText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    rating.setNameForSmile(BaseRating.GOOD, smileyRatingText4.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        smileyRatingText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    rating.setNameForSmile(BaseRating.GREAT, smileyRatingText5.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //add question, answers and question type to Firebase Database for Questions with text answers.
        textQuestionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference newChildRef = database.push();
                String key = newChildRef.getKey();

                questionEdit = (EditText) view.findViewById(R.id.questionEdit);
                String questionText = questionEdit.getText().toString();
                String questionType = spinner.getSelectedItem().toString();
                answer1Text = (EditText) view.findViewById(R.id.answer1Text);
                String answer1 = answer1Text.getText().toString();
                answer2Text = (EditText) view.findViewById(R.id.answer2Text);
                String answer2 = answer2Text.getText().toString();
                answer3Text = (EditText) view.findViewById(R.id.answer3Text);
                String answer3 = answer3Text.getText().toString();
                answer4Text = (EditText) view.findViewById(R.id.answer4Text);
                String answer4 = answer4Text.getText().toString();

                long count1, count2, count3, count4;
                count1 = count2 = count3 = count4 = 0;
                database.child(key).setValue(new Question(questionText, questionType, answer1, count1, answer2, count2, answer3, count3,
                        answer4, count4));

                database.child(key).child("begin").setValue(dateMilliSecBegin);
                database.child(key).child("end").setValue(dateMilliSecEnd);

                clearEdittexts();
                clearPreviewTextviews();
                showToastQuestionAdded();


            }
        });

        imageQuestionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageDateBegin = (EditText) view.findViewById(R.id.imageDateBegin);
                imageDateEnd = (EditText) view.findViewById(R.id.imageDateEnd);

                database.child(questionEdit.getText().toString()).child("Fragentyp").setValue(spinner.getSelectedItem().toString());
                database.child(questionEdit.getText().toString()).child("Antwort 1").setValue(answer1Image.getText().toString());
                database.child(questionEdit.getText().toString()).child("Antwort 2").setValue(answer2Image.getText().toString());
                database.child(questionEdit.getText().toString()).child("Antwort 3").setValue(answer3Image.getText().toString());
                database.child(questionEdit.getText().toString()).child("Antwort 4").setValue(answer4Image.getText().toString());
                database.child(questionEdit.getText().toString()).child("Beschreibung 1").setValue(imageDescription1.getText().toString());
                database.child(questionEdit.getText().toString()).child("Beschreibung 2").setValue(imageDescription2.getText().toString());
                database.child(questionEdit.getText().toString()).child("Beschreibung 3").setValue(imageDescription3.getText().toString());
                database.child(questionEdit.getText().toString()).child("Beschreibung 4").setValue(imageDescription4.getText().toString());
                database.child(questionEdit.getText().toString()).child("Antwort 1 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Antwort 2 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Antwort 3 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Antwort 4 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Von").setValue(dateMilliSecBegin);
                database.child(questionEdit.getText().toString()).child("Bis").setValue(dateMilliSecEnd);

                clearEdittexts();
                clearPreviewTextviews();
                showToastQuestionAdded();

            }
        });

        ratingQuestionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingDateBegin = (EditText) view.findViewById(R.id.ratingDateBegin);
                ratingDateEnd = (EditText) view.findViewById(R.id.ratingDateEnd);

                database.child(questionEdit.getText().toString()).child("Fragentyp").setValue(spinner.getSelectedItem().toString());
                database.child(questionEdit.getText().toString()).child("Rating 1").setValue(smileyRatingText1.getText().toString());
                database.child(questionEdit.getText().toString()).child("Rating 2").setValue(smileyRatingText2.getText().toString());
                database.child(questionEdit.getText().toString()).child("Rating 3").setValue(smileyRatingText3.getText().toString());
                database.child(questionEdit.getText().toString()).child("Rating 4").setValue(smileyRatingText4.getText().toString());
                database.child(questionEdit.getText().toString()).child("Rating 5").setValue(smileyRatingText5.getText().toString());
                database.child(questionEdit.getText().toString()).child("Rating 1 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Rating 2 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Rating 3 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Rating 4 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Rating 5 Clicks").setValue(0);
                database.child(questionEdit.getText().toString()).child("Von").setValue(dateMilliSecBegin);
                database.child(questionEdit.getText().toString()).child("Bis").setValue(dateMilliSecEnd);

                clearEdittexts();
                clearPreviewTextviews();
                showToastQuestionAdded();


            }
        });

        setDatePickerDialog(view);
        return view;
    }

    public void showToastQuestionAdded(){
        Toast toast = Toast.makeText(getContext(), "Frage hinzugefügt!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setDatePickerDialog(View view){
        EditText textDateBegin = view.findViewById(R.id.textDateBegin);
        datePickerDialogEditText(textDateBegin);

        EditText textDateEnd = view.findViewById(R.id.textDateEnd);
        datePickerDialogEditText(textDateEnd);

        EditText imageDateBegin = view.findViewById(R.id.imageDateBegin);
        datePickerDialogEditText(imageDateBegin);

        EditText imageDateEnd = view.findViewById(R.id.imageDateEnd);
        datePickerDialogEditText(imageDateEnd);

        EditText ratingDateBegin = view.findViewById(R.id.ratingDateBegin);
        datePickerDialogEditText(ratingDateBegin);

        EditText ratingDateEnd = view.findViewById(R.id.ratingDateEnd);
        datePickerDialogEditText(ratingDateEnd);
    }

    public void datePickerDialogEditText(EditText dateText){
        final Calendar myCalendar = Calendar.getInstance();

        final EditText edittext= (EditText) dateText;
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edittext, myCalendar);

            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }



    private void updateLabel(EditText editText, Calendar myCalendar) {
        EditText edittext = editText;
        String myFormat = "dd.MM.yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        if ((edittext.getId() == R.id.textDateBegin) || (edittext.getId() == R.id.imageDateBegin) || (edittext.getId() == R.id.ratingDateBegin)){
            dateMilliSecBegin = myCalendar.getTimeInMillis();
        } else if ((edittext.getId() == R.id.textDateEnd) || (edittext.getId() == R.id.imageDateEnd) || (edittext.getId() == R.id.ratingDateEnd)){
            dateMilliSecEnd = myCalendar.getTimeInMillis();
        }
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    //change layout displayed in editor for every Spinner item clicked
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(spinner.getSelectedItem().toString()){
            case "Antworten als Text":
                clearEdittexts();
                clearPreviewTextviews();
                viewflipperEditor.setDisplayedChild(0);
                viewflipperPreview.setDisplayedChild(0);
                break;
            case "Antworten als Icons":
                clearEdittexts();
                clearPreviewTextviews();
                viewflipperEditor.setDisplayedChild(1);
                viewflipperPreview.setDisplayedChild(1);
                break;
            case "Bewertung":
                clearEdittexts();
                clearPreviewTextviews();
                viewflipperEditor.setDisplayedChild(2);
                viewflipperPreview.setDisplayedChild(2);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setupAlertDialogWithWebview(){
        webviewDialogBuilder = new AlertDialog.Builder(getActivity());
        webviewDialog = webviewDialogBuilder.setView(iconsWebview).show();
    }

    public void setupWebview(){
        iconsWebview = new WebView(getActivity()){
            @Override
            public boolean onCheckIsTextEditor() {
                return true;
            }
        };
        iconsWebview.setWebViewClient(new WebViewClient());
        iconsWebview.loadUrl("http://www.flaticon.com");
        setWebSettings();
    }

    public void setWebSettings(){
        WebSettings webSettings = iconsWebview.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        iconsWebview.setFocusable(true);
        iconsWebview.setFocusableInTouchMode(true);
        iconsWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        iconsWebview.getSettings().setDomStorageEnabled(true);
        iconsWebview.getSettings().setAppCacheEnabled(true);
        iconsWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }


    public void clearEdittexts(){
        questionEdit.setText("");
        answer1Text.setText("");
        answer2Text.setText("");
        answer3Text.setText("");
        answer4Text.setText("");
        smileyRatingText1.setText("");
        smileyRatingText2.setText("");
        smileyRatingText3.setText("");
        smileyRatingText4.setText("");
        smileyRatingText5.setText("");
        textDateBegin.setText("");
        textDateEnd.setText("");
        imageDateBegin.setText("");
        imageDateEnd.setText("");
        ratingDateBegin.setText("");
        ratingDateEnd.setText("");
        dateMilliSecEnd = dateMilliSecBegin = 0;
    }

    public void clearPreviewTextviews(){
        questionPreview.setText("Deine Frage");
        answer1TextPreview.setText("1. Antwort");
        answer2TextPreview.setText("2. Antwort");
        answer3TextPreview.setText("3. Antwort");
        answer4TextPreview.setText("4. Antwort");

        answer1ImagePreview.setImageResource(R.drawable.tiger_icon);
        answer2ImagePreview.setImageResource(R.drawable.tiger_icon);
        answer3ImagePreview.setImageResource(R.drawable.tiger_icon);
        answer4ImagePreview.setImageResource(R.drawable.tiger_icon);

    }

    /*public void setDateEditTexts(View view){
        textDateBegin = (EditText) view.findViewById(R.id.textDateBegin);
        textDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(textDateBegin);
            }
        });

        textDateEnd = (EditText) view.findViewById(R.id.textDateEnd);
        textDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(textDateEnd);
            }
        });

        imageDateBegin = (EditText) view.findViewById(R.id.imageDateBegin);
        imageDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(imageDateBegin);
            }
        });

        imageDateEnd = (EditText) view.findViewById(R.id.imageDateEnd);
        imageDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(imageDateEnd);
            }
        });

        ratingDateBegin = (EditText) view.findViewById(R.id.ratingDateBegin);
        ratingDateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(ratingDateBegin);
            }
        });

        ratingDateEnd = (EditText) view.findViewById(R.id.ratingDateEnd);
        ratingDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(ratingDateEnd);
            }
        });


    }

    public void DateDialog(final EditText editText){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, year, month, day);
        dpDialog.show();
    }*/


}