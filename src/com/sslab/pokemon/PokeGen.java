package com.sslab.pokemon;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.sslab.pokemon.data.PokemonIndividualData;
import com.sslab.pokemon.data.PokemonSpeciesData;
import com.sslab.pokemon.data.PokemonValueData;
import com.sslab.pokemon.sprite.PokemonSprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by jerry on 2017/3/19.
 */
public class PokeGen {
    private JComboBox speciesComboBox;
    private JPanel root;

    private JButton deleteButton;
    private JButton saveButton;

    private JPanel slot0;
    private JPanel slot1;
    private JPanel slot2;
    private JPanel slot3;
    private JPanel slot4;
    private JPanel slot5;
    private JPanel slot6;
    private JPanel slot7;
    private JPanel slot8;
    private JTextField nickNameField;
    private JTextField hpField;
    private JTextField atkField;
    private JTextField defField;
    private JTextField spAtkField;
    private JTextField spDefField;
    private JTextField speedField;
    private JPanel currentSelectedPanel;
    private ArrayList<JTextField> statField;
    private JTextField currentSelectedField=hpField;
    Pokedex pokedex;
    HashMap<JPanel, PokemonIndividualData> pokemonMap;

    public PokeGen() {
        statField = new ArrayList<>();
        //TODO: Add the "stat" labels into statField
        statField.add(hpField);
        statField.add(atkField);
        statField.add(defField);
        statField.add(spAtkField);
        statField.add(spDefField);
        statField.add(speedField);
        //TODO: Use Pokedex to get pokemon species data
        pokedex=new Pokedex("bin/pokemonData.json");
        currentSelectedPanel=slot0;
        currentSelectedPanel.setBorder(BorderFactory.createBevelBorder(1));
        pokemonMap=new HashMap<>();
        //TODO: Add items into combobox. Each item should be a concat string of pokemon id and name from pokedex
        speciesComboBox.addItem("----------------");
        for(int i=0;i<pokedex.getPokemonSize();i++)
        {
            String str= Integer.toString(pokedex.getPokemonData(i).getId());
            str=str.concat(":");
            str=str.concat(pokedex.getPokemonData(i).getSpeciesName());
            speciesComboBox.addItem(str);
        }

        speciesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO update fields when select items in combobox
                int id=speciesComboBox.getSelectedIndex();
                if(id>0)
                {
                    int [] arr=new int[6];
                    arr=pokedex.getPokemonData(id-1).getSpeciesValue().getValArray();
                    for(int i=0;i<6;i++)
                    {
                        statField.get(i).setText("0");
                    }
                    JLabel currentLabel = (JLabel) currentSelectedPanel.getComponent(0);
                    setPokemonIcon(id - 1, currentLabel);

                }
                else
                {
                    for(int i=0;i<6;i++)
                    {
                        statField.get(i).setText("0");
                    }
                    nickNameField.setText("");
                    JLabel currentLabel = (JLabel) currentSelectedPanel.getComponent(0);
                    currentLabel.setIcon(null);
                }

            }
        });
        MouseListener click = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(pokemonMap.containsKey(currentSelectedPanel))
                {
                    JLabel currentLabel = (JLabel) currentSelectedPanel.getComponent(0);
                    int id=speciesComboBox.getSelectedIndex();
                    setPokemonIcon(id-1, currentLabel);

                }
                else
                {
                    speciesComboBox.setSelectedIndex(0);
                }
                currentSelectedPanel.setBorder(BorderFactory.createEtchedBorder());
                currentSelectedPanel=(JPanel)e.getComponent();
                currentSelectedPanel.setBorder(BorderFactory.createBevelBorder(1));
                loadPokemon(currentSelectedPanel);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        slot0.addMouseListener(click);
        slot1.addMouseListener(click);
        slot2.addMouseListener(click);
        slot3.addMouseListener(click);
        slot4.addMouseListener(click);
        slot5.addMouseListener(click);
        slot6.addMouseListener(click);
        slot7.addMouseListener(click);
        slot8.addMouseListener(click);
        MouseListener text = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(Integer.parseInt(currentSelectedField.getText())<0)
                    currentSelectedField.setText("0");
                else if(Integer.parseInt(currentSelectedField.getText())>32)
                    currentSelectedField.setText("32");
                currentSelectedField=(JTextField) e.getComponent();

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        hpField.addMouseListener(text);
        atkField.addMouseListener(text);
        defField.addMouseListener(text);
        spAtkField.addMouseListener(text);
        spDefField.addMouseListener(text);
        speedField.addMouseListener(text);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                setPokemon(currentSelectedPanel);

                try {
                    saveFile("morris_new_pokemon.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                pokemonMap.remove(currentSelectedPanel);
                speciesComboBox.setSelectedIndex(0);
                try {
                    saveFile("morris_new_pokemon.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setPokemonIcon(int id,JLabel label)
    {
        ImageIcon icon = new ImageIcon(PokemonSprite.getSprite(id));
        label.setIcon(icon);
    }
    public void setPokemon(JPanel panel) {
        int id = speciesComboBox.getSelectedIndex();
        if(id>0)
        {
            int [] arr=new int[6];
            for(int i=0;i<6;i++)
            {
                arr[i]=Integer.parseInt(statField.get(i).getText());
            }
            PokemonValueData valueArray = new PokemonValueData(arr);
            String nick=nickNameField.getText();
            String name=pokedex.getPokemonData(id-1).getSpeciesName();
            PokemonIndividualData pokemon=new PokemonIndividualData(pokedex.getPokemonData(id-1).getId(),name,nick,valueArray);
            pokemonMap.put(panel,pokemon);
        }

    }

    public void loadPokemon(JPanel panel) {
        if(pokemonMap.containsKey(panel))
        {

            PokemonIndividualData pokemon = pokemonMap.get(panel);
            String str = Integer.toString(pokemon.getId());
            str=str.concat(":");
            str=str.concat(pokemon.getSpeciesName());
            speciesComboBox.setSelectedItem(str);
            int[] arr = pokemon.getSpeciesValue().getValArray();
            for (int i = 0; i < 6; i++) {
                statField.get(i).setText(Integer.toString(arr[i]));
            }
            nickNameField.setText(pokemon.getNickName());
        }
        else
        {
            speciesComboBox.setSelectedIndex(0);
        }

    }
    void saveFile(String fileName) throws IOException {
        //TODO sort list before save to file
        ArrayList<PokemonIndividualData> pokemonIndividualList=new ArrayList<>();
        for(PokemonIndividualData poke:pokemonMap.values())
        {
            pokemonIndividualList.add(poke);
        }
        Collections.sort(pokemonIndividualList);
        //Create JsonWriter with fileName
        JsonWriter writer = new JsonWriter(new FileWriter(fileName));
        //create a gson object
        Gson gson = new Gson();
        //use gson to write object into json file, remember to convert ArrayList back to normal array first
        gson.toJson(pokemonIndividualList.toArray(),PokemonSpeciesData[].class,writer);
        //close the writer, very important!!!
        writer.close();

    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("PokeGen");
        frame.setContentPane(new PokeGen().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //change the icon of frame
        ImageIcon img = new ImageIcon("bin/pokemonball.png");
        frame.setIconImage(img.getImage());

    }

}