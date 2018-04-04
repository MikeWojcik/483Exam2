package rose.wojcikmg.exam2wojcikmg;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by kingm on 3/17/2018.
 */

public class StateAdaptor extends RecyclerView.Adapter<StateAdaptor.ViewHolder> implements ItemTouchHelperAdaptor{

    private Context myContext;
    ArrayList<stateCardWrapper> mStates = new ArrayList<>();
    private ArrayList<Territory> allStates;
    private Random mRandom = new Random();
    public static int score;
    private int lastTappedCard;
    private MainActivity main;
    private stateCardWrapper[] sizeChoices;



    public StateAdaptor(Context context){
        sizeChoices = new stateCardWrapper[0];
        myContext = context;
        allStates = new ArrayList<>();
        lastTappedCard = -1;
        allStates.addAll(FileUtils.loadFromJsonArray(myContext));
        score = 0;

        for(int i =0; i < 5; i++){
           addState();
        }


    }

    public StateAdaptor(Context context, MainActivity main){
        this(context);
        this.main = main;
    }

    public void setNumItems(int num){
        mStates = new ArrayList<>();
        for(int i = 0; i < num; i++){
            addState();
        }
        notifyDataSetChanged();
        score = 0;
        updateScore();
    }


    @Override
    public int getItemCount() {
        return mStates.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Territory current = mStates.get(position).territory;

        //customize name
        String name = current.name;
        TextView nameTextView = holder.stateName;
        nameTextView.setText(name);

        mStates.get(position).cardview = holder.card;

    }

    public void addState(){
        //gets a random index
        int rand = mRandom.nextInt(allStates.size());

        //loops until all states are unique
        for(stateCardWrapper c: mStates){
        if(c.territory.equals(allStates.get(rand))) {
            addState();
            return;
        }
        }
        //deletes from unused states so quiz can end after all states are done
        mStates.add(new stateCardWrapper(allStates.get(rand), null));

    }

    public void tappedForCapital(int pos){
        Snackbar snack = Snackbar.make(main.view,mStates.get(pos).territory.capital, Snackbar.LENGTH_INDEFINITE );

        View snackbarView = snack.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
        snack.show();

        lastTappedCard = pos;

    }

    public void areaModeSelected(){
        sizeChoices = new stateCardWrapper[2];

        sizeChoices[0] = mStates.get(mRandom.nextInt(mStates.size()));

        //adds two states from the menu into the sizeChoices array, making sure that they are not duplicates
               int toHighlight ;

              do {
                  toHighlight = mRandom.nextInt(mStates.size());
              }while(mStates.get(toHighlight).equals(sizeChoices[0]));


               sizeChoices[1] = mStates.get(toHighlight);

               for(stateCardWrapper c : sizeChoices){
               c.cardview.setCardBackgroundColor(myContext.getResources().getColor(R.color.highlighted));
           }

       }

    public void shuffle(){

        Collections.shuffle(mStates);
        notifyDataSetChanged();
//        ArrayList<stateCardWrapper> temp = new ArrayList<>();
//        temp.addAll(mStates);
//
//        mStates = new ArrayList<>();
//        notifyDataSetChanged();
//
//        while(temp.size()!= 0){
//            int posToGet = mRandom.nextInt(temp.size());
//            mStates.add(temp.get(posToGet));
//            temp.remove(posToGet);
//        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position, int direction) {

        if(position == lastTappedCard) {
            checkCapitalResponse(position, direction);
        }

        //handles invalid swiping
        else {
            Territory reAdd = mStates.get(position).territory;
            mStates.remove(position);
            notifyItemRemoved(position);

            mStates.add(position, new stateCardWrapper(reAdd, null));
            notifyItemInserted(position);


            //handles swiping away during Area Mode
            if (sizeChoices.length > 0) {
                for (int i = 0; i < 2; i++) {
                    if (reAdd.equals(sizeChoices[i].territory)) {
                        areaModeCheckResponse(i);
                        return;
                    }
                }

            }
        }

//starts congradulations activity Handler
        startCongrats();

    }

    private void startCongrats() {
        if(mStates.size() == 0){
            Intent intent = new Intent(myContext, FullscreenActivity.class);
            myContext.startActivity(intent);

        }
    }

    private void checkCapitalResponse(int position, int direction) {
        if (direction == 16) {
            score -=1;


        } else if (direction == 32) {
            score +=2;
        }
        lastTappedCard = -1;
        updateScore();
        mStates.remove(position);
        notifyItemRemoved(position);
    }

    public void updateScore(){
        main.getSupportActionBar().setTitle(main.getResources().getQuantityString(R.plurals.score, this.score, this.score));
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder{
        TextView stateName;
        CardView card;


        public ViewHolder(View retView){
            super(retView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    //deals with capital senario
                    if(sizeChoices.length ==0 ) {
                        tappedForCapital(getAdapterPosition());
                    }

                    //handles the area possibility
                    else{
                        for(int i = 0; i < 2; i++){
                            if(sizeChoices[i].cardview.equals(card)){
                                areaModeCheckResponse(i);
                                return;
                            }
                        }
                    }
                }

            });

            stateName = retView.findViewById(R.id.stateTextView);
            card = retView.findViewById(R.id.cardStateView);


                    }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    private void areaModeCheckResponse(int i) {
        String temp = "";
        int otherSizeChoice = (i + 1)%2;

        if(sizeChoices[i].territory.area > sizeChoices[otherSizeChoice].territory.area){

            temp = sizeChoices[i].territory.toString() + " > " + sizeChoices[otherSizeChoice].territory.toString();
            Snackbar.make(main.view, main.getString(R.string.yesStr)+" " +temp ,  Snackbar.LENGTH_INDEFINITE ).show();
            score +=4;
        }
        else if(sizeChoices[i].territory.area < sizeChoices[otherSizeChoice].territory.area){
            temp = sizeChoices[i].territory.toString() + " < " + sizeChoices[otherSizeChoice].territory.toString();
            Snackbar.make(main.view, main.getString(R.string.noStr)+ " " +temp ,  Snackbar.LENGTH_INDEFINITE ).show();
            score-=3;

        }
        updateScore();

        for(stateCardWrapper c : sizeChoices){
            c.cardview.setCardBackgroundColor(myContext.getColor(R.color.whiteColor));
        }
        sizeChoices = new stateCardWrapper[0];


    }

    public class stateCardWrapper {
        public Territory territory;
        public CardView cardview;

        public stateCardWrapper(Territory t , CardView c){
            territory=t;
            cardview = c;
        }

    }

        }
