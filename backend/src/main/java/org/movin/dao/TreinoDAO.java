package org.movin.dao;

import org.movin.config.ConnectionFactory;
import org.movin.model.Treino;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TreinoDAO {

    public boolean salvar(Treino treino) {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return false;
            }

            String sql = "INSERT INTO treino (titulo) VALUES (?)";

            comando = conexao.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            comando.setString(1, treino.getTitulo());
            comando.executeUpdate();

            resultado = comando.getGeneratedKeys();

            if (resultado.next()) {
                treino.setId(resultado.getInt(1));
            }

            return true;

        } catch (Exception erro) {
            System.out.println("Erro ao salvar treino.");
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


    public List<Treino> listar() {
        List<Treino> treinos = new ArrayList<>();

        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return treinos;
            }

            String sql = "SELECT id_treino, titulo FROM treino";

            comando = conexao.prepareStatement(sql);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                Treino treino = new Treino();

                treino.setId(resultado.getInt("id_treino"));
                treino.setTitulo(resultado.getString("titulo"));

                treinos.add(treino);
            }

        } catch (Exception erro) {
            System.out.println("Erro ao listar treinos.");
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

        return treinos;
    }


    public boolean atualizar(Treino treino) {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return false;
            }

            String sql = "UPDATE treino SET titulo = ? WHERE id_treino = ?";

            comando = conexao.prepareStatement(sql);

            comando.setString(1, treino.getTitulo());
            comando.setInt(2, treino.getId());

            int linhasAlteradas = comando.executeUpdate();

            return linhasAlteradas > 0;

        } catch (Exception erro) {
            System.out.println("Erro ao atualizar treino.");
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

            String sql = "DELETE FROM treino WHERE id_treino = ?";

            comando = conexao.prepareStatement(sql);
            comando.setInt(1, id);

            int linhasExcluidas = comando.executeUpdate();

            return linhasExcluidas > 0;

        } catch (Exception erro) {
            System.out.println("Erro ao excluir treino.");
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


    public int contarExercicios(int idTreino) {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if (conexao == null) {
                return 0;
            }

            String sql = "SELECT COUNT(*) FROM exercicio WHERE id_treino = ?";

            comando = conexao.prepareStatement(sql);
            comando.setInt(1, idTreino);

            resultado = comando.executeQuery();

            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (Exception erro) {
            System.out.println("Erro ao contar exercicios.");
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

        return 0;
    }
}