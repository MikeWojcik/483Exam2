package rose.wojcikmg.exam2wojcikmg;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kingm on 3/17/2018.
 */

public class StateAdaptor extends RecyclerView.Adapter<StateAdaptor.ViewHolder> implements ItemTouchHelperAdaptor{

    private Context myContext;
    ArrayList<cardViewWrapper> mStates = new ArrayList<>();
    private ArrayList<Territory> allStates;
    private Random mRandom = new Random();
    private int score;
    private int currentCapitalSelection;
    private MainActivity m;
    private cardViewWrapper[] sizeChoices;



    public StateAdaptor(Context context){
        sizeChoices = new cardViewWrapper[0];
        myContext = context;
        allStates = new ArrayList<>();
        currentCapitalSelection = -1;
        allStates.addAll(FileUtils.loadFromJsonArray(myContext));
        score = 0;

        for(int i =0; i < 5; i++){
           addState();
        }


    }

    public StateAdaptor(Context context, MainActivity main){
        this(context);
        m = main;
    }

    public void setNumItems(int num){
        mStates = new ArrayList<>();
        for(int i = 0; i < num; i++){
            addState();
        }
        notifyDataSetChanged();
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
        if(mStates.contains(allStates.get(rand))){
            addState();
            return;
        }
        //deletes from unused states so quiz can end after all states are done
        mStates.add(new cardViewWrapper(allStates.get(rand), null));

    }

    public void removeStateCapital(int pos){
        Snackbar.make(MainActivity.view,mStates.get(pos).territory.capital, Snackbar.LENGTH_INDEFINITE ).show();
        currentCapitalSelection = pos;

    }

    public void biggerState(){
        sizeChoices = new cardViewWrapper[2];

       for(int i = 0; i < 2; i++) {
           int toHighlight = mRandom.nextInt(mStates.size());
           sizeChoices[i] = mStates.get(toHighlight);

           sizeChoices[i].cardview.setCardBackgroundColor(m.getResources().getColor(R.color.highlighted));
       }


    }


    public void shuffle(){
        ArrayList<cardViewWrapper> temp = new ArrayList<>();
        temp.addAll(mStates);

        mStates = new ArrayList<>();
        notifyDataSetChanged();

        while(temp.size()!= 0){
            int posToGet = mRandom.nextInt(temp.size());
            mStates.add(temp.get(posToGet));
            temp.remove(posToGet);
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position, int direction) {
        Log.d("Testing", direction + "DirFirst");
        if(position == currentCapitalSelection) {

            Log.d("Testing", direction + "Dir");
            if (direction == 16) {
                score -=1;
                Log.d("Testing", "Left");

            } else if (direction == 32) {
                score +=2;
            }
            currentCapitalSelection = -1;
            updateScore();
            mStates.remove(position);
            notifyItemRemoved(position);
        }
        else{
            Log.d("Testing", "onItemDismiss: outOfPlace");
            Log.d("Testing", mStates.size() + "");
        }

    }

    public void updateScore(){
        m.getSupportActionBar().setTitle(m.getResources().getQuantityString(R.plurals.score, this.score, this.score));
        Log.d("Testing", "Score" + score);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder{
        TextView stateName;
        CardView card;
        boolean isHighlightedl;


        public ViewHolder(View retView){
            super(retView);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    //deals with normal senario
                    if(sizeChoices.length ==0) {
                        removeStateCapital(getAdapterPosition());
                    }
                    else{
                        for(int i = 0; i < 2; i++){
                            if(sizeChoices[i].cardview.getId() == card.getId()){
                                checkBiggerStateCorrect(i);
                            }
                        }
                    }
                }

            });

            stateName = retView.findViewById(R.id.stateTextView);
            card = retView.findViewById(R.id.cardStateView);


                    }
d

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    private void checkBiggerStateCorrect(int i) {
        if(s)
    }

    public class cardViewWrapper{
        public Territory territory;
        public CardView cardview;

        public cardViewWrapper(Territory t , CardView c){
            territory=t;
            cardview = c;
        }

    }

        }
