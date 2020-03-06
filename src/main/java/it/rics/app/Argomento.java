package it.rics.app;

public class Argomento {
  private String NomeArgomento;
  private String Riferimento;

  public String getMateria() {
    return Materia;
  }

  public void setMateria(String materia) {
    Materia = materia;
  }

  private String Materia;

  public Argomento(String nomeArgomento, String riferimento, String materia) {
    NomeArgomento = nomeArgomento;
    Riferimento = riferimento;
    Materia = materia;
  }


  public String getNomeArgomento() {
    return NomeArgomento;
  }

  public void setNomeArgomento(String nomeArgomento) {
    NomeArgomento = nomeArgomento;
  }

  public String getRiferimento() {
    return Riferimento;
  }

  public void setRiferimento(String riferimento) {
    Riferimento = riferimento;
  }
}
