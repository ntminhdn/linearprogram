package com.example.minhnt.linearprogram;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NumberVarDialog.OnItemSelectedListener {

    private RecyclerView rvInput;
    private ListAdapter adapter;
    private List<List<ItemObject>> list = new ArrayList<>();
    private NumberVarDialog dialog;
    private int numVar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ListAdapter(list);

        dialog = new NumberVarDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(), "first");

        rvInput = (RecyclerView) findViewById(R.id.rvInput);
        rvInput.setHasFixedSize(false);
        rvInput.setLayoutManager(new LinearLayoutManager(this));
        rvInput.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 10, 0, 10);
            }
        });
        rvInput.setAdapter(adapter);

        findViewById(R.id.btnSolve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PointValuePair solution = solve(list);
                    StringBuilder resutl = new StringBuilder("Phương án tối ưu:\n");
                    for (int i = 0; i < solution.getPoint().length; i++) {
                        resutl.append("x" + (i + 1) + ": " + solution.getPoint()[i] + "\n");
                    }
                    resutl.append("Kết quả tối ưu = " + solution.getValue());
                    showMessage(MainActivity.this, resutl.toString());
                } catch (NoFeasibleSolutionException e) {
                    showMessage(MainActivity.this, "Không có phương án tối ưu");
                } catch (UnboundedSolutionException ex) {
                    showMessage(MainActivity.this, "Có vô số phương án tối ưu");
                }
            }
        });
    }

    private PointValuePair solve(List<List<ItemObject>> listInput) {
        List<ItemObject> funcInput = listInput.get(0);
        ItemObject funcObjects = funcInput.get(0);
        GoalType goalType = GoalType.MAXIMIZE;

        if ((int) funcObjects.object == 1) {
            goalType = GoalType.MINIMIZE;
        }

        Log.d("hihi", "goalType " + goalType.toString());

        ArrayList<Double> inputFuncList = new ArrayList<>();
        for (int i = 0; i < funcInput.size() - 1; i++) {
            if ((2 * i) + 1 >= funcInput.size()) {
                break;
            }

            inputFuncList.add((double) funcInput.get((2 * i) + 1).object);
        }

        double[] inputFunc = new double[inputFuncList.size()];
        for (int i = 0; i < inputFuncList.size(); i++) {
            inputFunc[i] = inputFuncList.get(i);
            Log.d("hihi", "inputFunc " + inputFunc[i]);
        }

        Collection constraints = new ArrayList();
        for (int i = 1; i < listInput.size(); i++) {
            List<ItemObject> itemObjects = listInput.get(i);

            double[] constraint = new double[inputFunc.length];
            for (int j = 0; j < constraint.length; j++) {
                constraint[j] = (double) itemObjects.get(j * 2).object;
                Log.d("hihi", "constraint " + constraint[j]);
            }

            Relationship relationship = Relationship.LEQ;
            if ((int) itemObjects.get(itemObjects.size() - 2).object == 1) {
                relationship = Relationship.EQ;
            } else if ((int) itemObjects.get(itemObjects.size() - 2).object == 2) {
                relationship = Relationship.GEQ;
            }

            double constant = (double) itemObjects.get(itemObjects.size() - 1).object;

            constraints.add(new LinearConstraint(constraint, relationship, constant));
            Log.d("hihi", "relation " + relationship.toString());
            Log.d("hihi", "constant " + constant);
        }

        LinearObjectiveFunction f = new LinearObjectiveFunction(inputFunc, 0);
        return new SimplexSolver().optimize(f, constraints, goalType, true);
    }

    public List<ItemObject> addBlankData(int num) {
        List<ItemObject> list = new ArrayList<>();
        list.add(new ItemObject(ItemObject.INPUT, 0.0, 1));

        for (int i = 0; i < num - 1; i++) {
            list.add(new ItemObject(ItemObject.PLUS));
            list.add(new ItemObject(ItemObject.INPUT, 0.0, i + 2));
        }

        list.add(new ItemObject(ItemObject.RELATIONSHIP, 0));
        list.add(new ItemObject(ItemObject.CONSTANT, 0.0));

        return list;
    }

    public List<ItemObject> addFunc(int num) {
        List<ItemObject> list = new ArrayList<>();
        list.add(new ItemObject(FuncAdapter.FUNC_OBJECTS, 0));

        list.add(new ItemObject(FuncAdapter.FUNC_VAR, 0.0, 1));

        for (int i = 0; i < num - 1; i++) {
            list.add(new ItemObject(FuncAdapter.FUNC_PLUS));
            list.add(new ItemObject(FuncAdapter.FUNC_VAR, 0.0, i + 2));
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_add:
                list.add(addBlankData(numVar));
                adapter.notifyDataSetChanged();
                break;
            case R.id.ic_edit:
                NumberVarDialog numberVarDialog = new NumberVarDialog();
                numberVarDialog.setListener(this);
                numberVarDialog.show(getFragmentManager(), "other");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelectedListener(int numVar, int numFunc) {
        this.numVar = numVar;
        list.clear();
        list.add(addFunc(numVar));
        for (int i = 0; i < numFunc; i++) {
            list.add(addBlankData(numVar));
        }
        adapter.notifyDataSetChanged();
    }

    public static void showMessage(@NonNull Context context, String message) {
        new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(message).setPositiveButton(android.R.string.ok, null).show();
    }
}
