# Chess-Py Game

## Installation:
Make sure you have [python 3](https://www.python.org/downloads/release/python-385/) installed in your system.

### Linux
```
sudo apt install python3-pip
pip3 -m install --user pygame
python3 chess.py
```

### Windows
```
pip -m install --user pygame
python chess.py
```
---
## Playing the Game:

#### Moving A Piece On Your Turn:

Use the mouse to select one of your pieces and then select the square you want to move it to.
If you selected one of your pieces, the square that piece is on will be highlighted
by a purple square.

If you choose an invalid place to move to, a message will be displayed informing
you of the rule infringement, and you will be able to try again. If you decide
to not move this piece, just select it again to cancel the move, and the purple
square will go away to indicate that you deselected that piece.

#### Checkmate/End Game:

If there is indeed a checkmate, the game will end and the
winner will be displayed. To play again, close the game, and run it again.
