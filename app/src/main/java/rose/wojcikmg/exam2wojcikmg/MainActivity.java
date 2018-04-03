package rose.wojcikmg.exam2wojcikmg;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private StateAdaptor mStateAdaptor;
    public static RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateAdaptor = new StateAdaptor(this, MainActivity.this);

        mStateAdaptor.updateScore();

        view = (RecyclerView)findViewById(R.id.list_view);

        view.setAdapter(mStateAdaptor);
        view.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback= new SwipeItemTouchCallback(mStateAdaptor);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(view);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_settings:
                settingsMenu();
                    return true;

            case R.id.action_order:
                    mStateAdaptor.biggerState();
                    return true;

            case R.id.action_shuffle:
                mStateAdaptor.shuffle();
                return true;

            default:
                return true;
            }
        }

        private void settingsMenu(){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View settingView = getLayoutInflater().inflate(R.layout.dialog_settings, null, false);
            builder.setView(settingView);

            builder.setItems(getResources().getStringArray(R.array.settings_items), new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    switch(i){
                        case 0:
                            mStateAdaptor.setNumItems(5);
                            return;

                        case 1:
                            mStateAdaptor.setNumItems(10);
                            return;

                        case 2:
                            mStateAdaptor.setNumItems(50);
                            return;

                        default:
                            Log.d("Testing", "Error on Settings Select");
                            return;
                    }

                }
            });
            builder.create().show();

        }


    }
