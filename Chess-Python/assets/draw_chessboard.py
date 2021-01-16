from PIL import Image, ImageDraw
from itertools import cycle
import PIL


def draw_chessboard(n=8, pixel_width=60 * 8):
    def sq_start(i):
        return i * pixel_width / n

    def square(i, j):
        return list(map(sq_start, [i, j, i + 1, j + 1]))

    image = Image.new("RGB", (pixel_width, pixel_width), (214, 171, 111))
    
    draw_square = ImageDraw.Draw(image).rectangle
    squares = (square(i, j)
               for i_start, j in zip(cycle((0, 1)), list(range(n)))
               for i in range(i_start, n, 2))

    for sq in squares:
        draw_square(sq, fill='
        
    squares = (square(i, j)
               for i_start, j in zip(cycle((0, 1)), list(range(n)))
               for i in range(i_start, n, 2))
    
    
    image.save("chessboard.png")



draw_chessboard(8)
