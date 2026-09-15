package org.movin.dao;

import org.movin.config.ConnectionFactory;
import org.movin.model.Exercicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExercicioDAO {

    public boolean salvar(int idTreino, Exercicio exercicio) {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return false;
            }

            String sql = "INSERT INTO exercicio " +
                    "(id_treino, nome, series, repeticoes, carga_kg) " +
                    "VALUES (?, ?, ?, ?, ?)";

            comando = conexao.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            comando.setInt(1, idTreino);
            comando.setString(2, exercicio.getNome());
            comando.setInt(3, exercicio.getSeries());
            comando.setInt(4, exercicio.getRepeticoes());
            comando.setDouble(5, exercicio.getKg());

            comando.executeUpdate();

            resultado = comando.getGeneratedKeys();

            if (resultado.next()) {
                exercicio.setId(resultado.getInt(1));
            }

            return true;

        } catch (Exception erro) {
            System.out.println("Erro ao salvar exercicio.");
            System.out.println(erro.getMessage());
            return false;

        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }

                if (comando != null) {
                    comando.close();
                }

                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception erro) {
                System.out.println("Erro ao fechar conexao.");
            }
        }
    }


    public List<Exercicio> listarPorTreino(int idTreino) {
        List<Exercicio> exercicios = new ArrayList<>();

        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return exercicios;
            }

            String sql = "SELECT id_exercicio, nome, series, repeticoes, carga_kg " +
                    "FROM exercicio WHERE id_treino = ?";

            comando = conexao.prepareStatement(sql);
            comando.setInt(1, idTreino);

            resultado = comando.executeQuery();

            while (resultado.next()) {
                Exercicio exercicio = new Exercicio();

                exercicio.setId(resultado.getInt("id_exercicio"));
                exercicio.setNome(resultado.getString("nome"));
                exercicio.setSeries(resultado.getInt("series"));
                exercicio.setRepeticoes(resultado.getInt("repeticoes"));
                exercicio.setKg(resultado.getDouble("carga_kg"));

                exercicios.add(exercicio);
            }

        } catch (Exception erro) {
            System.out.println("Erro ao listar exercicios.");
            System.out.println(erro.getMessage());

        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }

                if (comando != null) {
                    comando.close();
                }

                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception erro) {
                System.out.println("Erro ao fechar conexao.");
            }
        }

        return exercicios;
    }


    public Exercicio buscarPorId(int id) {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return null;
            }

            String sql = "SELECT id_exercicio, nome, series, repeticoes, carga_kg " +
                    "FROM exercicio WHERE id_exercicio = ?";

            comando = conexao.prepareStatement(sql);
            comando.setInt(1, id);

            resultado = comando.executeQuery();

            if (resultado.next()) {
                Exercicio exercicio = new Exercicio();

                exercicio.setId(resultado.getInt("id_exercicio"));
                exercicio.setNome(resultado.getString("nome"));
                exercicio.setSeries(resultado.getInt("series"));
                exercicio.setRepeticoes(resultado.getInt("repeticoes"));
                exercicio.setKg(resultado.getDouble("carga_kg"));

                return exercicio;
            }

        } catch (Exception erro) {
            System.out.println("Erro ao buscar exercicio.");
            System.out.println(erro.getMessage());

        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }

                if (comando != null) {
                    comando.close();
                }

                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception erro) {
                System.out.println("Erro ao fechar conexao.");
            }
        }

        return null;
    }


    public boolean atualizar(Exercicio exercicio) {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return false;
            }

            String sql = "UPDATE exercicio SET nome = ?, series = ?, " +
                    "repeticoes = ?, carga_kg = ? WHERE id_exercicio = ?";

            comando = conexao.prepareStatement(sql);

            comando.setString(1, exercicio.getNome());
            comando.setInt(2, exercicio.getSeries());
            comando.setInt(3, exercicio.getRepeticoes());
            comando.setDouble(4, exercicio.getKg());
            comando.setInt(5, exercicio.getId());

            int linhasAlteradas = comando.executeUpdate();

            return linhasAlteradas > 0;

        } catch (Exception erro) {
            System.out.println("Erro ao atualizar exercicio.");
            System.out.println(erro.getMessage());
            return false;

        } finally {
            try {
                if (comando != null) {
                    comando.close();
                }

                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception erro) {
                System.out.println("Erro ao fechar conexao.");
            }
        }
    }


    public boolean excluir(int id) {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return false;
            }

            String sql = "DELETE FROM exercicio WHERE id_exercicio = ?";

            comando = conexao.prepareStatement(sql);
            comando.setInt(1, id);

            int linhasExcluidas = comando.executeUpdate();

            return linhasExcluidas > 0;

        } catch (Exception erro) {
            System.out.println("Erro ao excluir exercicio.");
            System.out.println(erro.getMessage());
            return false;

        } finally {
            try {
                if (comando != null) {
                    comando.close();
                }

                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception erro) {
                System.out.println("Erro ao fechar conexao.");
            }
        }
    }
}