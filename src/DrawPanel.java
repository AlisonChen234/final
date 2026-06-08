import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;

class DrawPanel extends JPanel implements MouseListener {

    private Deck deck;
    private Card[][] cards;
    private int selectedCount;

    public DrawPanel() {
        addMouseListener(this);

        deck = new Deck();
        cards = new Card[3][3];

        replenishBoard();
    }

    private void replenishBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (cards[row][col] == null || cards[row][col].isSelected()) {
                    cards[row][col] = deck.getRandomCard();
                }

            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int startX = 50;
        int startY = 10;

        g.drawRect(300, 100, 100, 50);
        g.drawString("Test", 330, 130);

        g.drawRect(300, 175, 100, 50);
        g.drawString("Restart", 325, 205);

        boolean boardEmpty = true;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Card card = cards[row][col];

                if (card != null) {

                    boardEmpty = false;

                    int x = startX + row * 75;
                    int y = startY + col * 90;

                    g.drawImage(card.getImage(), x, y, null);

                    card.setHitbox(x, y);

                    if (card.isSelected()) {
                        g.drawRect(
                                x,
                                y,
                                card.getImage().getWidth(),
                                card.getImage().getHeight()
                        );
                    }

                } else {

                    int x = startX + row * 75;
                    int y = startY + col * 90;

                    g.drawRect(x, y, 70, 85);
                    g.drawString("EMPTY", x + 15, y + 45);
                }
            }
        }

        g.drawString("There are " + deck.getDeck().size() + " cards left", 50, 300);

        if (boardEmpty && deck.getDeck().size() == 0) {
            g.drawString("YOU WIN!", 50, 330);
        }
        else if (!hasLegalMoves()) {
            g.drawString("GAME OVER! NO MOVES LEFT.", 50, 330);
        }
    }

    public void mousePressed(MouseEvent e) {

        int mouseX = e.getX();
        int mouseY = e.getY();

        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        if (mouseX >= 300 && mouseX <= 400 &&
                mouseY >= 100 && mouseY <= 150) {

            if (selectedCount == 3) {
                checkJQK();
            }
            else if (selectedCount == 2) {
                checkElevens();
            }
        }

        else if (mouseX >= 300 && mouseX <= 400 &&
                mouseY >= 175 && mouseY <= 225) {

            deck = new Deck();
            cards = new Card[3][3];
            selectedCount = 0;

            replenishBoard();
        }

        else {

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    Card card = cards[row][col];

                    if (card != null && card.inHitbox(mouseX, mouseY)) {

                        if (!card.isSelected()) {

                            if (selectedCount < 3) {
                                card.setSelected(true);
                                selectedCount++;
                            }

                        } else {

                            card.setSelected(false);
                            selectedCount--;
                        }
                    }
                }
            }
        }

        repaint();
    }

    private void checkJQK() {

        boolean hasJ = false;
        boolean hasQ = false;
        boolean hasK = false;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Card card = cards[row][col];

                if (card != null && card.isSelected()) {

                    String value = card.getValue();

                    if (value.equals("J")) {
                        hasJ = true;
                    }

                    if (value.equals("Q")) {
                        hasQ = true;
                    }

                    if (value.equals("K")) {
                        hasK = true;
                    }
                }
            }
        }

        if (hasJ && hasQ && hasK) {
            replenishBoard();
        }

        resetSelected();
    }

    private void checkElevens() {

        if (selectedValueSum() == 11) {
            replenishBoard();
        }

        resetSelected();
    }

    private int selectedValueSum() {

        int sum = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Card card = cards[row][col];

                if (card != null && card.isSelected()) {
                    sum += getCardValue(card);
                }
            }
        }

        return sum;
    }

    private boolean hasLegalMoves() {

        for (int i = 0; i < 9; i++) {

            Card card1 = cards[i / 3][i % 3];

            for (int j = i + 1; j < 9; j++) {

                Card card2 = cards[j / 3][j % 3];

                if (card1 != null && card2 != null) {

                    if (getCardValue(card1) + getCardValue(card2) == 11) {
                        return true;
                    }
                }
            }
        }

        boolean hasJ = false;
        boolean hasQ = false;
        boolean hasK = false;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                Card card = cards[row][col];

                if (card != null) {

                    String value = card.getValue();

                    if (value.equals("J")) {
                        hasJ = true;
                    }

                    if (value.equals("Q")) {
                        hasQ = true;
                    }

                    if (value.equals("K")) {
                        hasK = true;
                    }
                }
            }
        }

        return hasJ && hasQ && hasK;
    }

    private int getCardValue(Card card) {

        String value = card.getValue();

        if (value.equals("A")) {
            return 1;
        }

        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    private void resetSelected() {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (cards[row][col] != null) {
                    cards[row][col].setSelected(false);
                }
            }
        }

        selectedCount = 0;
    }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void mouseClicked(MouseEvent e) { }
}