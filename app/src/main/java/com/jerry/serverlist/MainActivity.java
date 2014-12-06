package com.jerry.serverlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;
import com.ibm.mobile.services.data.internal.IBMBaaSImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends Activity {
    public static final String CLASS_NAME = "MainActivity";

    private static final String APP_ID = "applicationID";
    private static final String APP_SECRET = "applicationSecret";
    private static final String APP_ROUTE = "applicationRoute";
    private static final String PROPS_FILE = "serverList.properties";

    public static final int EDIT_ACTIVITY_RC = 1;
    ArrayList<Chespirito> itemList = new ArrayList<Chespirito>();
    //ServerListApplication blApplication;
    ListItemAdapter lvAdapter;
    //ActionMode mActionMode = null;
    int listItemPosition;
    //public static final String CLASS_NAME="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Read from properties file
        Properties props = new java.util.Properties();
        Context context = getApplicationContext();
        try {
            AssetManager assetManager = context.getAssets();
            props.load(assetManager.open(PROPS_FILE));
            Log.i("MainActivity", "Found configuration file: " + PROPS_FILE);
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "The serverList.properties file was not found.", e);
        } catch (IOException e) {
            Log.e("MainActivity", "The serverList.properties file could not be read properly.", e);
        }


        // initialize the IBM core backend-as-a-service
        IBMBluemix.initialize(this, props.getProperty(APP_ID), props.getProperty(APP_SECRET), props.getProperty(APP_ROUTE));
        // initialize the IBM Data Service
        IBMData.initializeService();
        // register the Item Specialization
        Chespirito.registerSpecialization(Chespirito.class);

        //Vars
        Button add = (Button) findViewById(R.id.acceptText);
        Button clear = (Button) findViewById(R.id.clearText);
        ListView itemsLV = (ListView)findViewById(R.id.itemsList);

        /* Use application class to maintain global state. */
        itemList = getItemList();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createItem(v);

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText(v);
            }
        });

		/* Set up the array adapter for items list view. */
        lvAdapter = new ListItemAdapter (getBaseContext(), itemList);
        itemsLV.setAdapter(lvAdapter);
        /* Refresh the list. */
        listItems();

        //set on click listeners
        itemsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //show header of the item clicked on a toast
                Toast.makeText(getBaseContext(), itemList.get(i).getPersonaje() + " pressed", Toast.LENGTH_SHORT).show();
            }
        });

		/* Set long click listener. */
        /*itemsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /* Called when the user long clicks on the textview in the list. */
/*            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long rowId) {
                listItemPosition = position;
                if (mActionMode != null) {
                    return false;
                }
		        /* Start the contextual action bar using the ActionMode.Callback. */
/*                mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
                return true;
            }
        });*/

    }

    /**
     * Called on done and will add item to list.
     *
     * @param  v edittext View to get item from.
     */
    public void createItem(View v) {
        EditText txtPersonaje = (EditText) findViewById(R.id.personaje);
        EditText txtActor = (EditText) findViewById(R.id.autor);
        String personaje = txtPersonaje.getText().toString();
        String actor = txtActor.getText().toString();
        if (!personaje.equals("")) {
            Chespirito chesp = new Chespirito(personaje, actor);

            // Use the IBMDataObject to create and persist the Item object.
            chesp.save().continueWith(new Continuation<IBMDataObject, Void>() {

                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
                    // Log if the save was cancelled.
                    if (task.isCancelled()){
                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the save task fails.
                    else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    }

                    // If the result succeeds, load the list.
                    else {
                        listItems();
                    }
                    return null;
                }

            });

            // Set text field back to empty after item is added.
            txtPersonaje.setText("");
            txtActor.setText("");
        }
    }
    /*
    public void createItem(View v) {
        EditText txtPersonaje = (EditText) findViewById(R.id.personaje);
        EditText txtActor = (EditText) findViewById(R.id.autor);
        String personaje = txtPersonaje.getText().toString();
        String actor = txtActor.getText().toString();
        if (!personaje.equals("")) {
            Chespirito chesp = new Chespirito(personaje, actor);
            itemList.add(chesp);
            //sortItems(itemList);
            setItemList(itemList);
            lvAdapter.notifyDataSetChanged();
            // Set text field back to empty.
            txtPersonaje.setText("");
            txtActor.setText("");

        }
    }*/

    /**
     * Removes text on click of x button.
     *
     * @param  v the edittext view.
     */
    public void clearText(View v) {
        EditText personaje = (EditText) findViewById(R.id.personaje);
        EditText autor = (EditText) findViewById(R.id.autor);
        personaje.setText("");
        autor.setText("");
    }

    /**
     * On return from other activity, check result code to determine behavior.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
		/* If an edit has been made, notify that the data set has changed. */
            case EDIT_ACTIVITY_RC:
                //sortItems(itemList);
                lvAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Will delete an item from the list, based on last item to be long-clicked.
     *
     */
    public void deleteItem(Chespirito item) {
        itemList.remove(listItemPosition);

        // This will attempt to delete the item on the server.
        item.delete().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                // Log if the delete was cancelled.
                if (task.isCancelled()){
                    Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                }

                // Log error message, if the delete task fails.
                else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                }

                // If the result succeeds, reload the list.
                else {
                    lvAdapter.notifyDataSetChanged();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);

        lvAdapter.notifyDataSetChanged();
    }
    /*
    public void deleteItem(Chespirito item) {
		/* Remove item, set itemList of blApplication, and notify adapter of data change. */
/*        itemList.remove(listItemPosition);
        setItemList(itemList);
        lvAdapter.notifyDataSetChanged();
    }*/

    public ArrayList<Chespirito> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Chespirito> list) {
        itemList = list;
    }

    public void listItems() {
        try {
            IBMQuery<Chespirito> query = IBMQuery.queryForClass(Chespirito.class);  //Falla al hacer el query
            // Query all the Item objects from the server
            query.find().continueWith(new Continuation<List<Chespirito>, Void>() {

                @Override
                public Void then(Task<List<Chespirito>> task) throws Exception {
                    final List<Chespirito> objects = task.getResult();
                    // Log if the find was cancelled.
                    if (task.isCancelled()){
                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the find task fails.
                    else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    }


                    // If the result succeeds, load the list.
                    else {
                        // Clear local itemList.
                        // We'll be reordering and repopulating from DataService.
                        itemList.clear();
                        for(IBMDataObject item:objects) {
                            itemList.add((Chespirito) item);
                        }
                        //sortItems(itemList);
                        lvAdapter.notifyDataSetChanged();
                    }
                    return null;
                }
            },Task.UI_THREAD_EXECUTOR);

        }  catch (IBMDataException error) {
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
        }
    }

    /**
     * Sort a list of Items.
     * //@param List<Chespirito> theList
     */
    /*private void sortItems(List<Chespirito> theList) {
        // Sort collection by case insensitive alphabetical order.
        Collections.sort(theList, new Comparator<Chespirito>() {
            public int compare(Item lhs,
                               Item rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();
                return lhsName.compareToIgnoreCase(rhsName);
            }
        });
    }*/

/*    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        /* Inflate a menu resource with context menu items. */
/*            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * Called when user clicks on contextual action bar menu item.
         *
         * Determines which item was clicked, and then determine behavior appropriately.
         *
         * @param ActionMode mode and MenuItem item clicked
         */
/*        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Chespirito lItem = itemList.get(listItemPosition);
	    	/* Switch dependent on which action item was clicked. */
/*            switch (item.getItemId()) {
	    		/* On edit, get all info needed & send to new, edit activity. */
/*                case R.id.action_edit:
                    updateItem(lItem.getName());
                    mode.finish(); /* Action picked, so close the CAB. */
//                    return true;
	            /* On delete, remove list item & update. */
/*                case R.id.action_delete:
                    deleteItem(lItem);
                    mode.finish(); /* Action picked, so close the CAB. */
/*                default:
                    return false;
            }
        }

        /* Called on exit of action mode. */
/*        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
