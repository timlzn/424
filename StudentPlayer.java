package student_player;

import Saboteur.cardClasses.SaboteurCard;
import boardgame.Move;

import Saboteur.SaboteurPlayer;

import java.util.ArrayList;
import java.util.Map;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260803642");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        System.out.println("turn " + boardState.getTurnNumber());
        if (boardState.getTurnNumber() <= 2)
        {
            MyTools.clearTerminalNode();
        }
        MyTools.addOpponentMoveToTerminalNodes(boardState.getHiddenBoard());
//        MyTools.printTerminal();
        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
        if (MyTools.PREVIOUS_HAND.isEmpty())
        {
            for (SaboteurCard card : boardState.getCurrentPlayerCards())
            {
                MyTools.partitionHand(card);
            }
        }
        else
        {
            MyTools.partitionHand(boardState.getCurrentPlayerCards().get(boardState.getCurrentPlayerCards().size() - 1));
        }
        SaboteurMove myMove = null;
        ArrayList<SaboteurCard> hand = boardState.getCurrentPlayerCards();
        myMove = MyTools.checkMalus(legalMoves, hand);
        if (MyTools.MAX_HEIGHT > 10) myMove = MyTools.moveLeftRight(legalMoves);
        if (myMove == null) myMove = MyTools.moveDown(legalMoves, true);
        if (myMove == null) myMove = MyTools.moveDown(legalMoves, false);
        if (myMove == null) myMove = MyTools.aimDown(legalMoves);
        if (myMove == null) myMove = MyTools.checkDrop(legalMoves, hand);
        if (myMove == null) myMove = MyTools.checkDestroy(legalMoves, boardState);
        if (myMove == null) myMove = boardState.getRandomMove();
        MyTools.addOwnMoveToTerminalNodes((SaboteurMove) myMove, MyTools.TerminalNodes);
        MyTools.printTerminal();
        // Return your move to be processed by the server
//        System.out.println("Played move: " + myMove.getCardPlayed().getName() + " x position: " + myMove.getPosPlayed()[1] + " y position " + myMove.getPosPlayed()[0]);
        return myMove;
    }
}
