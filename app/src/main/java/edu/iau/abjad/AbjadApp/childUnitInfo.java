package edu.iau.abjad.AbjadApp;

import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class childUnitInfo {
    int score;
    Button Lesson;
    Button nextLesson;
    Button next2lesson;
    ImageView Lock,Stars;
    String letters,lessonId;
    double time;

    public childUnitInfo(int score, ImageView lock, ImageView stars, Button lesson, String letters) {
        this.score = score;
        Lock = lock;
        Stars = stars;
        Lesson = lesson;
        this.letters = letters;
    }

    public childUnitInfo(int score, String letters,double time) {
        this.score = score;
        this.letters = letters;
        this.time=time;

    }

    public Button getNext2lesson() {
        return next2lesson;
    }
    public void setNext2lesson(Button next2lesson) {
        this.next2lesson = next2lesson;
    }

    public Button getNextLesson() {
        return nextLesson;
    }

    public void setNextLesson(Button nextLesson) {
        this.nextLesson = nextLesson;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ImageView getLock() {
        return Lock;
    }

    public void setLock(ImageView lock) {
        Lock = lock;
    }

    public ImageView getStars() {
        return Stars;
    }

    public void setStars(ImageView stars) {
        Stars = stars;
    }

    public Button getLesson() {
        return Lesson;
    }

    public void setLesson(Button lesson) {
        Lesson = lesson;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }


}
