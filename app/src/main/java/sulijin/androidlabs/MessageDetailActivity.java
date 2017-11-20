package sulijin.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MessageDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        final long id = getIntent().getBundleExtra("bundle").getLong("id");
        bundle.putLong("id",id);
        bundle.putString("message", getIntent().getBundleExtra("bundle").getString("message"));
        messageFragment.setArguments(bundle);
        FragmentManager fragmentManager =getFragmentManager();
        //remove previous fragment
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.phoneFrameLayout, messageFragment).addToBackStack(null).commit();
    }

}
