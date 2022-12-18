package org.example.model;

import lombok.Data;
import org.jooq.meta.derby.sys.Sys;

import java.util.ArrayList;
import java.util.List;

@Data
public class Field {

    private Integer width = 9;
    private Integer height = 9;

    private Integer difficulty;
    private List<List<Cell>> cellsUnsolved;
    private List<List<Cell>> cellsSolved;
    private List<List<Cell>> cellsCurrent;

    /**
     * Конструктор - инициализация поля судоку
     * @param unsolved - исходное поле
     * @param solved - решение поля
     * @param difficulty - сложность поля
     */
    public Field(String unsolved, String solved, Integer difficulty){
        this.difficulty = difficulty;

        cellsUnsolved = new ArrayList<>();
        cellsSolved = new ArrayList<>();
        int ch = 0;
        for(int i = 0; i < height; i++){
            cellsUnsolved.add(new ArrayList<>());
            cellsSolved.add(new ArrayList<>());
            for(int j = 0; j < width; j++, ch++){
                Cell cellUnsolved = new Cell();
                int unsolvedValue = unsolved.charAt(ch) - '0';
                cellUnsolved.setValue(unsolvedValue);
                cellUnsolved.setIsChangeable(unsolvedValue == 0);
                cellsUnsolved.get(i).add(cellUnsolved);

                Cell cellSolved = new Cell();
                int solvedValue = solved.charAt(ch) - '0';
                cellSolved.setValue(solvedValue);
                cellSolved.setIsChangeable(false);
                cellsSolved.get(i).add(cellSolved);
            }
        }
    }

    /**
     * Установка значения в клеточку поля
     * @param height - координата y
     * @param width - координата x
     * @param value - значение
     */
    public void setValue(Integer height, Integer width, Integer value){
        cellsUnsolved.get(height).get(width).setValue(value);
    }

    /**
     * Проверка правильности заполненного поля
     * @return - флаг правильности
     */
    public Boolean check() {
        boolean check = true;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                check = check && cellsUnsolved.get(i).get(j).getValue().equals(cellsSolved.get(i).get(j).getValue());
            }
        }
        return check;
    }

    /**
     * Конвертация теекущего поля в строку
     * @return строка с текущим состоянием поля
     */
    public String fieldString(){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.append(cellsUnsolved.get(i).get(j).getValue().toString());
            }
        }
        return result.toString();
    }

    /**
     * Вывод поля в консоль
     */
    public void showField(){
        System.out.println("-----field------");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(cellsUnsolved.get(i).get(j).getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
