package br.com.eseffair.consultapontoeletronico;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConsultaPontoActivity extends Activity {
	
	private static final String PREFS_NAME = "MyPrefsFile";
	private EditText etSetor;
	private EditText etCpf;
	private EditText etMatricula;
	private CheckBox chxSalvPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_ponto);
        
        /** Monta o Combobox dos Meses. */
        Spinner spMes = (Spinner) this.findViewById(R.id.spMes);
        ArrayAdapter<String> adapterMes = montaComboboxMes();
        spMes.setAdapter(adapterMes);
        
        /** Monta o Combobox dos Anos. */
        Spinner spAno = (Spinner) this.findViewById(R.id.spAno);
        ArrayAdapter<Integer> adapterAno = montaComboboxAno();
        spAno.setAdapter(adapterAno);
        
        /** Carrega campos. */
		etMatricula = (EditText) findViewById(R.id.etMatricula);
		etSetor = (EditText) findViewById(R.id.etSetor);
		etCpf = (EditText) findViewById(R.id.etCpf);
		chxSalvPref = (CheckBox) findViewById(R.id.chxSalvPref);
		
		/** Carregar do arquivo de preferências os campos Matricula, Setor e Cpf. */
        carregarCamposDePreferencia();
        
        /** Executa a consulta. */
        Button btConsultar = (Button) this.findViewById(R.id.btConsultar);
        btConsultar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Spinner spMes = (Spinner) findViewById(R.id.spMes);
				Spinner spAno = (Spinner) findViewById(R.id.spAno);
				
		    	/** Validação de Campos. */
				if (validarCampos()) {
					/** Salvar no arquivo de preferências os campos Matricula, Setor e Cpf. */
					salvarCamposEmPreferencia();
					
					String matricula = etMatricula.getText().toString();
					String setor = etSetor.getText().toString();
					String cpf = etCpf.getText().toString();
					String mesSelecionado = (String) spMes.getSelectedItem();
					String keyMesSelecionado = getSescrMes(mesSelecionado);
					String anoSelecionado = String.valueOf(spAno.getSelectedItem());
					
					String url = "http://online.sefaz.am.gov.br/pontoeletronico/ResultadoConsulta_oracle.asp" + 
							"?matricula=" + matricula.toUpperCase() + 
							"&setor=" + setor.toUpperCase() + 
							"&cpf=" + cpf + 
							"&Mes=" + keyMesSelecionado + 
							"&ano=" + anoSelecionado;
					
					Intent intent = new Intent(ConsultaPontoActivity.this, ResultadoConsultaPontoActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
				}
			}
		});
    }

	protected ArrayAdapter<String> montaComboboxMes() {
		final String[] meses = new DateFormatSymbols(new Locale("pt_BR")).getMonths();
        ArrayAdapter<String> adapterMes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meses);
        adapterMes.setDropDownViewResource(android.R.layout.simple_spinner_item);
		return adapterMes;
	}

	protected ArrayAdapter<Integer> montaComboboxAno() {
		final Integer[] anos = new Integer[10];
		anos[0] = new GregorianCalendar().get(Calendar.YEAR); // o primeiro ano é o ano atual.
		// os demais anos são anteriores ao ano atual.
		for (int i = 1; i < anos.length; i++) {
			anos[i] = anos[i-1] - 1;
		}
        ArrayAdapter<Integer> adapterAno = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, anos);
        adapterAno.setDropDownViewResource(android.R.layout.simple_spinner_item);
		return adapterAno;
	}
    
    protected void salvarCamposEmPreferencia() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	if (chxSalvPref.isChecked()) {
	    	editor.putString("matricula", etMatricula.getText().toString());
			editor.putString("setor", etSetor.getText().toString());
			editor.putString("cpf", etCpf.getText().toString());
			editor.putBoolean("salvarpref", chxSalvPref.isChecked());
    	} else {
    		editor.remove("matricula");
			editor.remove("setor");
			editor.remove("cpf");
			editor.putBoolean("salvarpref", chxSalvPref.isChecked());
    	}

        editor.commit();
	}
    
    protected void carregarCamposDePreferencia() {
    	 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	 
         String matricula = settings.getString("matricula", "");
         String setor = settings.getString("setor", "");
         String cpf = settings.getString("cpf", "");
         Boolean salvarpref = settings.getBoolean("salvarpref", false);
         
         if (matricula != null && !matricula.equals("")) {
        	 etMatricula.setText(matricula);
         }
         if (setor != null &&  !setor.equals("")) {
        	 etSetor.setText(setor);
         }
         if (cpf != null &&  !cpf.equals("")) {
        	 etCpf.setText(cpf);
         }
         if (salvarpref != null) {
        	 chxSalvPref.setChecked(salvarpref);
         } 
         
         if (salvarpref) {
        	 Toast.makeText(ConsultaPontoActivity.this, "Carregando preferências salvas!", Toast.LENGTH_LONG).show();
         }
	}

	protected String getSescrMes(String mesSelecionado) {
    	final Map<String,String> descrMes =  new HashMap<String, String>();
        descrMes.put("janeiro", "1");
        descrMes.put("fevereiro", "2");
        descrMes.put("março", "3");
        descrMes.put("abril", "4");
        descrMes.put("maio", "5");
        descrMes.put("junho", "6");
        descrMes.put("julho", "7");
        descrMes.put("agosto", "8");
        descrMes.put("setembro", "9");
        descrMes.put("outubro", "10");
        descrMes.put("novembro", "11");
        descrMes.put("dezembro", "12");
    	
    	return descrMes.get(mesSelecionado);
    }
    
	/** Validação de Campos. */
    protected boolean validarCampos() {
    	boolean flag = true;
    	
		if (etMatricula.getText() == null ||  etMatricula.getText().toString().equals("")) {
			 etMatricula.setError("O campo Matrícula não foi informado!");
			 flag = false;
		}
		if (etSetor.getText() == null ||  etSetor.getText().toString().equals("")) {
			etSetor.setError("O campo Setor não foi informado!");
			flag = false;
		}
		if (etCpf.getText() == null ||  etCpf.getText().toString().equals("")) {
			etCpf.setError("O campo Senha não foi informado!");
			flag = false;
		}
		
		return flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_consulta_ponto, menu);
        return true;
    }

    
}
