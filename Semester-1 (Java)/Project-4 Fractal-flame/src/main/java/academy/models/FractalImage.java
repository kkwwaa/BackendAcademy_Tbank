package academy.models;

public record FractalImage(int width, int height, Pixel[] pixels) {
    /** Фабричный метод для создания пустого изображения */
    public static FractalImage create(int width, int height) {
        Pixel[] pixels = new Pixel[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel();
        }
        return new FractalImage(width, height, pixels);
    }

    /** Проверка, находится ли точка внутри изображения */
    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /** Доступ к пикселю по координатам */
    public Pixel pixel(int x, int y) {
        return pixels[y * width + x];
    }
}
