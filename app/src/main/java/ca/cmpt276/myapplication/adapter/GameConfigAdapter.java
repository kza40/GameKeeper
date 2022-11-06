package ca.cmpt276.myapplication.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import java.util.List;
import ca.cmpt276.myapplication.AddConfig;
import ca.cmpt276.myapplication.R;
import ca.cmpt276.myapplication.ViewConfigs;
import ca.cmpt276.myapplication.model.ConfigManager;
import ca.cmpt276.myapplication.model.GameConfig;

public class GameConfigAdapter extends ArrayAdapter<GameConfig> {
    private static final String TAG="GamesListAdapter";
    private Context context;
    private int resource;

    public GameConfigAdapter(@NonNull Activity context, int resource, @NonNull List<GameConfig> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GameConfig gameConfig =getItem(position);
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        TextView title=convertView.findViewById(R.id.tvTitle);
        TextView poorScore=convertView.findViewById(R.id.tvPoorScore);
        TextView goodScore=convertView.findViewById(R.id.tvGoodScore);
        ImageButton btnEdit=convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete=convertView.findViewById(R.id.btnDelete);


        title.setText(gameConfig.getGameTitle());
        poorScore.setText("Poor Score: "+Integer.toString(gameConfig.getPoorScore()));
        goodScore.setText("Good Score: "+Integer.toString(gameConfig.getGoodScore()));
        setupClickListenersOnButton(parent,position,btnEdit,btnDelete);
        return convertView;
    }

    private void setupClickListenersOnButton(ViewGroup parent, int position, ImageButton btnEdit, ImageButton btnDelete) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=AddConfig.makeIntent(context,true,position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.confirm_dialog_message)
                        .setTitle(R.string.confirm_dialog_title)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                                switch (i) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        ConfigManager.getInstance().removeConfigAtIndex(position);
                                        notifyDataSetChanged();
                                        ((ViewConfigs)context).setEmptyState();
                                        break;
                                }
                            }
                        }).
                        setNegativeButton(android.R.string.no, null);

                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
