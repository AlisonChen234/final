import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.ArrayList;

class DrawPanel extends JPanel implements MouseListener {

    private Deck deck;
    private Card[][] cardlist;
    private int count = 0;

    public DrawPanel() {
        this.addMouseListener(this);
        deck = new Deck();
        cardlist = new Card[3][3];
        replenishBoard();
    }

    private void replenishBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cardlist[i][j] == null || cardlist[i][j].isSelected()) {
                    cardlist[i][j] = deck.getRandomCard();
                }
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 10;
        g.drawRect(300, 100, 100, 50);
        g.drawString("Test", 330, 130);
        g.drawRect(300, 175, 100, 50);
        g.drawString("Restart", 330, 205);
        boolean boardIsEmpty = true;


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Card card = cardlist[i][j];
                if (card != null) {
                    boardIsEmpty = false;
                    g.drawImage(card.getImage(), x + i * 75, y + j * 90, null);
                    card.setHitbox(x + i * 75, y + j * 90);

                    if (card.isSelected() && card.getImage() != null) {
                        g.drawRect(x + i * 75, y + j * 90, card.getImage().getWidth(), card.getImage().getHeight());
                    }
                } else {
                    g.drawRect(x + i * 75, y + j * 90, 70, 85);
                    g.drawString("EMPTY", x + i * 75 + 15, y + j * 90 + 45);
                }
            }
        }

        g.drawString("There are " + deck.getDeck().size() + " cards left", 50, 300);

        if (boardIsEmpty && deck.getDeck().size() == 0) {
            g.drawString("YOU WIN!", 50, 330);
        } else if (!hasLegalMoves()) {
            g.drawString("GAME OVER! NO MOVES LEFT.", 50, 330);
        }
    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (mouseX >= 300 && mouseX <= 400 && mouseY >= 100 && mouseY <= 150) {
                if (count == 3) {
                    checkAndProcessJQK();
                } else if (count == 2) {
                    checkAndProcessElevens();
                }
            }

            else if (mouseX >= 300 && mouseX <= 400 && mouseY >= 175 && mouseY <= 225) {
                deck = new Deck();
                cardlist = new Card[3][3];
                replenishBoard();
                count = 0;
            }

            else {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        Card card = cardlist[i][j];
                        if (card != null && card.inHitbox(mouseX, mouseY)) {
                            if (!card.isSelected()) {
                                if (count < 3) {
                                    card.setSelected(true);
                                    count++;
                                }
                            } else {
                                card.setSelected(false);
                                count--;
                            }
                        }
                    }
                }
            }
        }

        repaint();
    }

    private void checkAndProcessJQK() {
        ArrayList<String> requiredJQK = new ArrayList<>();
        requiredJQK.add("J");
        requiredJQK.add("Q");
        requiredJQK.add("K");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Card card = cardlist[i][j];
                if (card != null && card.isSelected()) {
                    requiredJQK.remove(card.getValue());
                }
            }
        }

        if (requiredJQK.isEmpty()) {
            replenishBoard();
        }
        resetSelected();
    }

    private void checkAndProcessElevens() {
        if (getValueSum() == 11) {
            replenishBoard();
        }
        resetSelected();
    }

    private int getValueSum() {
        int valueSum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Card card = cardlist[i][j];
                if (card != null && card.isSelected()) {
                    String val = card.getValue();
                    if (val.equals("A")) {
                        valueSum += 1;
                    } else {
                        try {
                            valueSum += Integer.parseInt(val);
                        } catch (NumberFormatException e) {
                            valueSum += 0;
                        }
                    }
                }
            }
        }
        return valueSum;
    }

    private boolean hasLegalMoves() {
        for (int i = 0; i < 9; i++) {
            Card card1 = cardlist[i / 3][i % 3];
            if (card1 == null) continue;

            for (int j = i + 1; j < 9; j++) {
                Card card2 = cardlist[j / 3][j % 3];
                if (card2 == null) continue;

                if (getCardNumericValue(card1) + getCardNumericValue(card2) == 11) {
                    return true;
                }
            }
        }

        boolean hasJ = false;
        boolean hasQ = false;
        boolean hasK = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Card card = cardlist[i][j];
                if (card != null) {
                    if (card.getValue().equals("J")) hasJ = true;
                    if (card.getValue().equals("Q")) hasQ = true;
                    if (card.getValue().equals("K")) hasK = true;
                }
            }
        }

        return hasJ && hasQ && hasK;
    }

    private int getCardNumericValue(Card card) {
        String val = card.getValue();
        if (val.equals("A")) return 1;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void resetSelected() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cardlist[i][j] != null) {
                    cardlist[i][j].setSelected(false);
                }
            }
        }
        count = 0;
    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}