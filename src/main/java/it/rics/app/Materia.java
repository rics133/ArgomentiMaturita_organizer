package it.rics.app;

public class Materia {
  private int id;
  private String NomeMateria;

  public Materia(int id, String nomeMateria) {
    this.id = id;
    NomeMateria = nomeMateria;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNomeMateria() {
    return NomeMateria;
  }

  public void setNomeMateria(String nomeMateria) {
    NomeMateria = nomeMateria;
  }
}
