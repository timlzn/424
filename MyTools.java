package student_player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurBonus;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDestroy;
import Saboteur.cardClasses.SaboteurMalus;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;
import boardgame.Move;


public class MyTools {
    // Each node is represented as y, x, 4 directions
    public static Integer[] InitialEntrance = new Integer[]{5, 5, 1, 1, 1, 1};
    public static LinkedList<Integer[]> TerminalNodes = new LinkedList<>();
    public static int GLOBAL_X = 5;
    public static int GLOBAL_Y = 5;
    public static Integer[] GLOBAL_DIRECTION = {1, 1, 1, 1};
    public static int MAX_HEIGHT = 5;

    public static int BOTTOM = 0;
    public static int LEFT = 1;
    public static int RIGHT = 2;
    public static int TOP = 3;

    public static boolean TERMINATE = false;

    public static ArrayList<SaboteurCard> PREVIOUS_HAND = new ArrayList<>();
    public static ArrayList<SaboteurCard> TopDown = new ArrayList<>();
    public static ArrayList<SaboteurCard> LeftRight = new ArrayList<>();
    public static ArrayList<SaboteurCard> TopLeftRight = new ArrayList<>();
    public static ArrayList<SaboteurCard> DeadEnd = new ArrayList<>();
    public static ArrayList<SaboteurCard> PathCut = new ArrayList<>();
    public static ArrayList<SaboteurCard> Special = new ArrayList<>();

    private static Integer[] direction_all = new Integer[]{1, 1, 1, 1};
    private static Integer[] direction_top_left_right = new Integer[]{0, 1, 1, 1};
    private static Integer[] direction_top_left = new Integer[]{0, 1, 0, 1};
    private static Integer[] direction_top_right = new Integer[]{0, 0, 1, 1};
    private static Integer[] direction_bottom_left_right = new Integer[]{1, 1, 1, 0};
    private static Integer[] direction_bottom_left = new Integer[]{1, 1, 0, 0};
    private static Integer[] direction_bottom_right = new Integer[]{1, 0, 1, 0};
    private static Integer[] direction_bottom_top_right = new Integer[]{1, 0, 1, 1};
    private static Integer[] direction_bottom_top_left = new Integer[]{1, 1, 0, 1};
    private static Integer[] direction_bottom_top = new Integer[]{1, 0, 0, 1};
    private static Integer[] direction_left_right = new Integer[]{0, 1, 1, 0};
    private static Integer[] dead_end = new Integer[]{0, 0, 0, 0};


    public static boolean isDeadEnd(Integer[] array)
    {
        boolean flag = true;
        for (int i = 0; i < 4; i ++)
        {
            if (array[2 + i] == 1) flag = false;
        }
        return flag;
    }

    public static void addNodeToTerminal(Integer[] array, int index)
    {
        if (array[1] > MAX_HEIGHT)
        {
            MAX_HEIGHT = array[1];
        }
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (TerminalNodes.get(i)[0] == array[0] && TerminalNodes.get(i)[1] == array[1])
            {
                return;
            }
        }
        if (TerminalNodes.get(0)[1] < array[1])
        {
            TerminalNodes.addFirst(array);
            return;
        }
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (TerminalNodes.get(i)[1] > array[1])
            {
                if (index != TerminalNodes.size() - 1)
                {
                    index = i + 1;
                    break;
                }
            }
        }
        if (index == 0)
        {
            if (TerminalNodes.get(index)[1] < array[1])
            {
                TerminalNodes.addFirst(array);
            }
            else
            {
                TerminalNodes.add(1, array);
            }
            return;
        }
        else if (index == TerminalNodes.size())
        {
            if (TerminalNodes.get(index)[1] < array[1])
            {
                TerminalNodes.add(TerminalNodes.size(), array);
            }
            else
            {
                TerminalNodes.addLast(array);
            }
            return;
        }
        else
        {
            if (TerminalNodes.get(index)[1] < array[1])
            {
                TerminalNodes.add(index, array);
            }
            else
            {
                TerminalNodes.add(index + 1, array);
            }
            return;
        }
    }

    public static void clearTerminalNode()
    {
        if (TerminalNodes.size() < 1) return;
        TerminalNodes.clear();
        TerminalNodes.add(new Integer[]{5, 5, 1, 1, 1, 1});
        PREVIOUS_HAND.clear();
    }

    public static void printTerminal()
    {
        for(int i = 0; i < TerminalNodes.size(); i++)
        {
            System.out.println("x: " + Integer.toString(TerminalNodes.get(i)[0]) + ", y: " + Integer.toString(TerminalNodes.get(i)[1]) + " available position " + Integer.toString(TerminalNodes.get(i)[2]) + Integer.toString(TerminalNodes.get(i)[3]) + Integer.toString(TerminalNodes.get(i)[4]) + Integer.toString(TerminalNodes.get(i)[5]));
        }
        System.out.println("\n");
    }

    public static void partitionHand(SaboteurCard currentCard)
    {
        if (PREVIOUS_HAND.isEmpty() || !PREVIOUS_HAND.contains(currentCard))
        {
            PREVIOUS_HAND.add(currentCard);
        }
        else
        {
            return;
        }
//        System.out.println("Name of the current card is " + currentCard.getName());
        String cardName = currentCard.getName();
        switch (cardName)
        {
            case "Bonus":
            case "Destroy":
            case "Malus":
            case "Map":
                Special.add(currentCard);
                break;
            case "Tile:0":
            case "Tile:6":
            case "Tile:6_flip":
                TopDown.add(currentCard);
                break;
            case "Tile:8":
                TopDown.add(currentCard);
                LeftRight.add(currentCard);
                break;
            case "Tile:13":
            case "Tile:3":
            case "Tile:3_flip":
            case "Tile:11":
            case "Tile:11_flip":
            case "Tile:14":
            case "Tile:14_flip":
            case "Tile:1":
            case "Tile:2":
            case "Tile:2_flip":
            case "Tile:15":
                PathCut.add(currentCard);
                break;
            case "Tile:4":
            case "Tile:4_flip":
            case "Tile:12":
                DeadEnd.add(currentCard);
                break;
            case "Tile:5":
            case "Tile:5_flip":
            case "Tile:7":
            case "Tile:7_flip":
            case "Tile:9":
            case "Tile:9_flip":
                TopLeftRight.add(currentCard);
                LeftRight.add(currentCard);
                break;
            case "Tile:10":
                LeftRight.add(currentCard);
                break;
        }
    }

    private static Integer[] getCardDirection(String idx, int previous_card)
    {
//        System.out.println("the command is " + idx);

        Map<String, Integer[]> mapping = new HashMap<String, Integer[]>();
        mapping.put("Tile:0", direction_bottom_top);
        mapping.put("Tile:1", direction_bottom_top);
        mapping.put("Tile:2", direction_bottom_top_right);
        mapping.put("Tile:2_flip", direction_bottom_top_left);
        mapping.put("Tile:3", direction_top_right);
        mapping.put("Tile:3_flip", direction_bottom_left);
        mapping.put("Tile:4", dead_end);
        mapping.put("Tile:4_flip", dead_end);
        mapping.put("Tile:5", direction_bottom_right);
        mapping.put("Tile:5_flip", direction_top_left);
        mapping.put("Tile:6", direction_bottom_top_left);
        mapping.put("Tile:6_flip", direction_bottom_top_right);
        mapping.put("Tile:7", direction_top_right);
        mapping.put("Tile:7_flip", direction_bottom_left);
        mapping.put("Tile:8", direction_all);
        mapping.put("Tile:9", direction_bottom_left_right);
        mapping.put("Tile:9_flip", direction_top_left_right);
        mapping.put("Tile:10", direction_left_right);
        mapping.put("Tile:11", direction_bottom_left_right);
        mapping.put("Tile:11_flip", direction_top_left_right);
        mapping.put("Tile:12", dead_end);
        mapping.put("Tile:12_flip", dead_end);
        mapping.put("Tile:13", direction_all);
        mapping.put("Tile:14", direction_top_left);
        mapping.put("Tile:14_flip", direction_bottom_right);
        mapping.put("Tile:15", direction_left_right);
        mapping.put("Drop", dead_end);
        mapping.put("Bonus", dead_end);
        mapping.put("hidden_1", direction_all);
        mapping.put("hidden_2", direction_all);

        Integer[] direction = mapping.get(idx);
        direction[3 - previous_card] = 0;
        return direction;
    }

    private static boolean searchAround(SaboteurTile[][] board, Integer[] coordinate_and_search)
    {
        int coordinate_x = coordinate_and_search[0];
        int coordinate_y = coordinate_and_search[1];

        if (board[coordinate_y][coordinate_x] == null)
        {
            if (getTerminalNodeWithXY(coordinate_x, coordinate_y) > 0)
            {
                TerminalNodes.remove(getTerminalNodeWithXY(coordinate_x, coordinate_y));
            }
            iterateDestroyed(coordinate_x, coordinate_y);
            TERMINATE = true;
            return false;
        }

        if (coordinate_y < 12)
        {
            if (coordinate_and_search[2 + BOTTOM] == 1 && board[coordinate_y + 1][coordinate_x] != null)
            {
                GLOBAL_X = coordinate_x;
                GLOBAL_Y = coordinate_y + 1;
                GLOBAL_DIRECTION = getCardDirection(board[coordinate_y + 1][coordinate_x].getName(), BOTTOM);
                return true;
            }
        }
        if (coordinate_x > 1)
        {
            if (coordinate_and_search[2 + LEFT] == 1 && board[coordinate_y][coordinate_x - 1] != null)
            {
                GLOBAL_X = coordinate_x - 1;
                GLOBAL_Y = coordinate_y;
                GLOBAL_DIRECTION = getCardDirection(board[coordinate_y][coordinate_x - 1].getName(), LEFT);
                return true;
            }
        }
        if (coordinate_x < 12)
        {
            if (coordinate_and_search[2 + RIGHT] == 1 && board[coordinate_y][coordinate_x + 1] != null)
            {
                GLOBAL_X = coordinate_x + 1;
                GLOBAL_Y = coordinate_y;
                GLOBAL_DIRECTION = getCardDirection(board[coordinate_y][coordinate_x + 1].getName(), RIGHT);
                return true;
            }
        }
        if (coordinate_y > 1)
        {
            if (coordinate_and_search[2 + TOP] == 1 && board[coordinate_y - 1][coordinate_x] != null)
            {
                GLOBAL_X = coordinate_x;
                GLOBAL_Y = coordinate_y - 1;
                GLOBAL_DIRECTION = getCardDirection(board[coordinate_y - 1][coordinate_x].getName(), TOP);
                return true;
            }
        }
        return false;
    }

    private static void iterateDestroyed(int x, int y)
    {
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (TerminalNodes.get(i)[0] == x && TerminalNodes.get(i)[1] == y + 1)
            {
                Integer[] myArray = TerminalNodes.get(i);
                myArray[2 + TOP] = 1;
                TerminalNodes.set(i, myArray);
            }
            if (TerminalNodes.get(i)[0] == x && TerminalNodes.get(i)[1] == y - 1)
            {
                Integer[] myArray = TerminalNodes.get(i);
                myArray[2 + BOTTOM] = 1;
                TerminalNodes.set(i, myArray);
            }
            if (TerminalNodes.get(i)[0] == x + 1 && TerminalNodes.get(i)[1] == y)
            {
                Integer[] myArray = TerminalNodes.get(i);
                myArray[2 + LEFT] = 1;
                TerminalNodes.set(i, myArray);
            }
            if (TerminalNodes.get(i)[0] == x - 1 && TerminalNodes.get(i)[1] == y)
            {
                Integer[] myArray = TerminalNodes.get(i);
                myArray[2 + RIGHT] = 1;
                TerminalNodes.set(i, myArray);
            }
        }
    }

    private static void updateNode(int node_position, int new_x, int new_y)
    {
        Integer[] previous_array = TerminalNodes.get(node_position);
        if (previous_array[0] > new_x)
        {
            previous_array[2 + LEFT] = 0;
        }
        if (previous_array[0] < new_x)
        {
            previous_array[2 + RIGHT] = 0;
        }
        if (previous_array[1] < new_y)
        {
            previous_array[2 + BOTTOM] = 0;
        }
        if (previous_array[1] > new_y)
        {
            previous_array[2 + TOP] = 0;
        }
        for (int i = 0; i < previous_array.length; i ++)
        {
            if (previous_array[i] == 1)
            {
                TerminalNodes.set(node_position, previous_array);
                return;
            }
        }
    }

    public static void addOpponentMoveToTerminalNodes(SaboteurTile[][] board)
    {
        if (TerminalNodes.size() < 1)
        {
            TerminalNodes.add(InitialEntrance);
        }
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (searchAround(board, TerminalNodes.get(i)))
            {
                if (TERMINATE) {
                    TERMINATE = false;
                    return;
                }
                updateNode(i, GLOBAL_X, GLOBAL_Y);
                Integer[] result = new Integer[]{GLOBAL_X, GLOBAL_Y, GLOBAL_DIRECTION[0], GLOBAL_DIRECTION[1], GLOBAL_DIRECTION[2], GLOBAL_DIRECTION[3]};
                System.out.println("Saving Opponent x: " + Integer.toString(result[0]) + ", y: " + Integer.toString(result[1]));
                addNodeToTerminal(result, i);
                return;
            };
        }
        System.out.println("No opponent move added");
    }

    private static int getTerminalNodeWithXY(int x, int y)
    {
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (TerminalNodes.get(i)[0] == x && TerminalNodes.get(i)[0] == y) return i;
        }
        return -1;
    }

    public static void addOwnMoveToTerminalNodes(SaboteurMove move, LinkedList<Integer[]> node)
    {
        if (move.getCardPlayed().getName().equals("Drop")) {
            System.out.println("No own move added");
            return;
        }
        if (move.getCardPlayed().getName().equals("Destroy"))
        {
            if (getTerminalNodeWithXY(move.getPosPlayed()[1], move.getPosPlayed()[0]) > 0)
            {
                TerminalNodes.remove();
            }
            iterateDestroyed(move.getPosPlayed()[1], move.getPosPlayed()[0]);
            return;
        }
        int pos_x = move.getPosPlayed()[1];
        int pos_y = move.getPosPlayed()[0];
        if (node.size() < 1)
        {
            node.add(InitialEntrance);
        }
        for (int i = 0; i < node.size(); i ++)
        {
            if (Math.abs(node.get(i)[0] - pos_x) == 1 && node.get(i)[1] == pos_y)
            {
//                System.out.println("played someting on the x axis");
                if (pos_x < 12)
                {
                    if (node.get(i)[0] > pos_x)
                    {
                        GLOBAL_X = pos_x;
                        GLOBAL_Y = pos_y;
                        GLOBAL_DIRECTION = getCardDirection(move.getCardPlayed().getName(), LEFT);
                    }

                }
                else if (pos_x > 1)
                {
                    GLOBAL_X = pos_x;
                    GLOBAL_Y = pos_y;
                    GLOBAL_DIRECTION = getCardDirection(move.getCardPlayed().getName(), RIGHT);
                }
            }
            if (Math.abs(node.get(i)[1] - pos_y) == 1 && node.get(i)[0] == pos_x)
            {
//                System.out.println("played someting on the y axis");
                if (pos_y > 1)
                {
                    if (node.get(i)[1] > pos_y)
                    {
                        GLOBAL_X = pos_x;
                        GLOBAL_Y = pos_y;
                        GLOBAL_DIRECTION = getCardDirection(move.getCardPlayed().getName(), TOP);
                    }
                }
                else if (pos_y < 12)
                {
                    GLOBAL_X = pos_x;
                    GLOBAL_Y = pos_y;
                    GLOBAL_DIRECTION = getCardDirection(move.getCardPlayed().getName(), BOTTOM);
                }
            }
            updateNode(i, GLOBAL_X, GLOBAL_Y);
            Integer[] result = new Integer[]{GLOBAL_X, GLOBAL_Y, GLOBAL_DIRECTION[0], GLOBAL_DIRECTION[1], GLOBAL_DIRECTION[2], GLOBAL_DIRECTION[3]};
            addNodeToTerminal(result, i);
            System.out.println("Saving Own x: " + Integer.toString(result[0]) + ", y: " + Integer.toString(result[1]));
            return;
        }
//        System.out.println("No own move added");
    }

    public static void printArrayList(ArrayList<SaboteurCard> cardList)
    {
        for (int i = 0; i < cardList.size(); i ++)
        {
            System.out.println("Card stored is " + cardList.get(i).getName());
        }
    }

    public static boolean arrayHasCard(ArrayList<SaboteurCard> cardList, String cardName)
    {
        for (int i = 0; i < cardList.size(); i ++)
        {
            if (cardName.length() > 5)
            {
//                System.out.println("they are " + cardList.get(i).getName() + " and " + cardName + " and "
//                        + (cardList.get(i).getName().equals(cardName.trim()) || cardList.get(i).getName().equals(cardName.substring(0, 5))));
                if (cardList.get(i).getName().equals(cardName.trim()) || cardList.get(i).getName().equals(cardName.substring(0, 5).trim()))
                {
                    return true;
                }
            }
            else {
                if (cardList.get(i).getName().equals(cardName))
                {
//                    System.out.println("they are " + cardList.get(i).getName() + " and " + cardName);
                    return true;
                }
            }
        }
        return false;
    }

    public static SaboteurMove moveDown(ArrayList<SaboteurMove> legalMove, boolean best)
    {
        int currentHeight = TerminalNodes.getFirst()[1];
//        MyTools.printArrayList(PathCut);
//        MyTools.printArrayList(DeadEnd);
        for (SaboteurMove move: legalMove)
        {
//            System.out.println("checking move: " + move.getCardPlayed().getName());
            if (arrayHasCard(Special, move.getCardPlayed().getName())
                    || arrayHasCard(DeadEnd, move.getCardPlayed().getName())
                    || arrayHasCard(PathCut, move.getCardPlayed().getName()))
            {
                continue;
            }
            if (best)
            {
                if (move.getPosPlayed()[0] > currentHeight && arrayHasCard(TopDown, move.getCardPlayed().getName()))
                {
//                    System.out.println("Selecting move: " + move.getCardPlayed().getName() + " x position: " + move.getPosPlayed()[1] + " y position " + move.getPosPlayed()[0]);
                    if (arrayHasCard(TopDown, move.getCardPlayed().getName())) TopDown.remove(move.getCardPlayed());
                    if (arrayHasCard(TopLeftRight, move.getCardPlayed().getName())) TopLeftRight.remove(move.getCardPlayed());
                    if (arrayHasCard(LeftRight, move.getCardPlayed().getName())) LeftRight.remove(move.getCardPlayed());
                    if (arrayHasCard(Special, move.getCardPlayed().getName())) Special.remove(move.getCardPlayed());
                    PREVIOUS_HAND.remove(move.getCardPlayed());
                    return move;
                }
            }
            else
            {
                if (move.getPosPlayed()[0] > currentHeight)
                {
                    if (arrayHasCard(DeadEnd, move.getCardPlayed().getName())) continue;
                    if (arrayHasCard(PathCut, move.getCardPlayed().getName())) continue;
//                    System.out.println("Selecting move: " + move.getCardPlayed().getName() + " x position: " + move.getPosPlayed()[1] + " y position " + move.getPosPlayed()[0]);
                    if (arrayHasCard(TopDown, move.getCardPlayed().getName())) TopDown.remove(move.getCardPlayed());
                    if (arrayHasCard(TopLeftRight, move.getCardPlayed().getName())) TopLeftRight.remove(move.getCardPlayed());
                    if (arrayHasCard(LeftRight, move.getCardPlayed().getName())) LeftRight.remove(move.getCardPlayed());
                    if (arrayHasCard(Special, move.getCardPlayed().getName())) Special.remove(move.getCardPlayed());
                    PREVIOUS_HAND.remove(move.getCardPlayed());
                    return move;
                }
            }
        }
        return null;
    }

    public static SaboteurMove checkDrop(ArrayList<SaboteurMove> legalMove, ArrayList<SaboteurCard> hand)
    {
        for(SaboteurMove move : legalMove) {
            if(move.getCardPlayed().getName().equals("Drop")) {
                String dropped = hand.get(move.getPosPlayed()[0]).getName();
                if(arrayHasCard(PathCut, dropped) || arrayHasCard(DeadEnd, dropped)) {
                    return move;
                }
            }
        }
        return null;
    }

    public static boolean closeTo(Integer[] position, Integer x, Integer y)
    {
        if (Math.abs(position[0] - x) == 1 && position[1] - y == 0)
        {
            return true;
        }
        return false;
    }

    public static SaboteurMove aimDown(ArrayList<SaboteurMove> legalMove)
    {
        for (int i = 0; i < TerminalNodes.size(); i ++)
        {
            for (SaboteurMove move : legalMove)
            {
                if (arrayHasCard(TopLeftRight, move.getCardPlayed().getName())
                        && !arrayHasCard(PathCut, move.getCardPlayed().getName())
                        && closeTo(TerminalNodes.get(i), move.getPosPlayed()[1], move.getPosPlayed()[0]))
                {
                    TopLeftRight.remove(move.getCardPlayed());
                    PREVIOUS_HAND.remove(move.getCardPlayed());
//                    System.out.println("Second best move: " + move.getCardPlayed().getName() + " x position: " + move.getPosPlayed()[1] + " y position " + move.getPosPlayed()[0]);
                    return move;
                }
            }
        }
        return null;
    }

    public static SaboteurMove moveLeftRight(ArrayList<SaboteurMove> legalMove)
    {
        for(int i = 0; i < TerminalNodes.size(); i ++)
        {
            if (TerminalNodes.get(i)[1] > 10)
            {
                for (SaboteurMove move: legalMove)
                {
                    if (arrayHasCard(LeftRight, move.getCardPlayed().getName()) && move.getPosPlayed()[0] == 12)
                        return move;

                    if (arrayHasCard(LeftRight, move.getCardPlayed().getName())
                            && (arrayHasCard(TopDown, move.getCardPlayed().getName())
                            || arrayHasCard(TopLeftRight, move.getCardPlayed().getName()))
                            && move.getPosPlayed()[1] == 11)
                        return move;
                }
            }
        }
        return null;
    }

    public static SaboteurMove checkMalus(ArrayList<SaboteurMove> legalMove, ArrayList<SaboteurCard> hand)
    {

        for(SaboteurMove move : legalMove) {
            if(move.getCardPlayed().getName().equals("Malus")) {
                return move;
            }

        }

        return null;
    }

    public static SaboteurMove checkDestroy(ArrayList<SaboteurMove> legalMove, SaboteurBoardState bs)
    {

        for(SaboteurMove move : legalMove) {
            if(move.getCardPlayed().getName().equals("Destroy")) {
                SaboteurTile[][] board = bs.getHiddenBoard();
                String destroyed = board[move.getPosPlayed()[0]][move.getPosPlayed()[1]].getName();
                if(arrayHasCard(PathCut, destroyed) || arrayHasCard(DeadEnd, destroyed)) {
                    return move;
                }
            }

        }
        return null;
    }
}
