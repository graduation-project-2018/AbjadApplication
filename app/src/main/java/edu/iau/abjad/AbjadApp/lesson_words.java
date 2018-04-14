package edu.iau.abjad.AbjadApp;


public class lesson_words {

    String content;
    String audio_file;
    String pic_file;
    int child_score;

    //Constructer
    public lesson_words(String content, String audio_file, String pic_file, int child_score){
        this.content = content;
        this.audio_file = audio_file;
        this.pic_file = pic_file;
        this.child_score = child_score;

    }
}


        /*r.ref.child("Lessons").child("lesson4").child("lesson_letter").setValue("ل");
        r.ref.child("Lessons").child("lesson3").child("unitID").setValue("unit1");

        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word1").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson4").child("Words").child("word2").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word3").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("Words").child("word4").child("pic_file").setValue("-");


        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence1").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence2").child("pic_file").setValue("-");

        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("audio_file").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentenرce3").child("content").setValue("-");
        r.ref.child("Lessons").child("lesson3").child("sentences").child("sentence3").child("pic_file").setValue("-");*/

            /*countDownTimer= new CountDownTimer(10000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                            System.out.println("onTick methed");
                                        }

                                        public void onFinish() {

                                            System.out.println("onFinish methed");
                                            playAudio(wordsArrayList.get(words_counter).audio_file);
                                        }
                                    }.start();*/
