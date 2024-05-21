package org.example;

import org.example.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic extends JPanel implements Runnable {
    private final ArrayList<Enemy> enemies; // Seznam nepřátel
    private final ArrayList<Wall> walls; // Seznam zdí
    private final ArrayList<Bullet> rockets; // Seznam raket
    private Heartz heartz; // Srdce hráče
    private Heartz heartz2; // Druhé srdce hráče
    private Heartz heartz3; // Třetí srdce hráče
    BackGround backGround; // Pozadí hry
    StartGamePic startGamePic; // Startovní obrázek
    EndGamePic endGamePic; // Koncový obrázek
    KeyReader keyReader = new KeyReader(); // Čtečka klávesnice
    private final Font customFont = new Font("Arial", Font.BOLD, 16); // Vlastní font pro texty
    public int gameState = 1; // Stav hry
    private boolean gameStarted = false; // Určuje, zda hra začala
    Thread gameThread; // Vlákno pro hru
    long currentTime; // Aktuální čas
    double delta = 0; // Časový rozdíl mezi snímky
    int fps = 60; // Počet snímků za sekundu
    long lastTime = System.nanoTime(); // Čas posledního snímku
    double drawInterval = 1000000000 / fps; // Interval mezi jednotlivými snímky
    public int width = 1080, height = 720; // Rozměry herního okna
    int secondsPassed; // Počet uplynulých sekund od začátku hry
    int startCount; // Počet snímků od posledního spawnu nepřátel
    int spawnRate = 27; // Rychlost spawnu nepřátel
    Player player = new Player(this, keyReader, 500, 500, "Player.gif"); // Hráč
    Timer timer; // Časovač pro měření uplynulého času


    public GameLogic() {
        this.enemies = new ArrayList<>(); // Inicializuje seznam nepřátel
        this.walls = new ArrayList<>(); // Inicializuje seznam zdí
        this.rockets = new ArrayList<>(); // Inicializuje seznam raket
        backGround = new BackGround(this); // Inicializuje pozadí hry
        startGamePic = new StartGamePic(this); // Inicializuje startovní obrázek
        endGamePic = new EndGamePic(this); // Inicializuje koncový obrázek
        setPreferredSize(new Dimension(width, height)); // Nastaví preferované rozměry herního panelu
        setBackground(Color.black); // Nastaví černé pozadí herního panelu
        setDoubleBuffered(true); // Povolí dvojité vykreslování
        setFocusable(true); // Nastaví herní panel jako zaměřitelný
        addKeyListener(keyReader); // Přidá čtečku klávesnice
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    startTimer(); // Spustí časovač po kliknutí myší, pokud hra ještě nezačala
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && gameState == 3) {
                    resetGame(); // Resetuje hru po stisku klávesy Enter v koncovém stavu hry
                }
            }
        });
    }


    public void initialize() {
        Wall wall1 = new Wall(400, 400,  "WallUp.png"); // Inicializuje první zeď
        Wall wall2 = new Wall(550, 450,  "WallVer.png"); // Inicializuje druhou zeď
        walls.add(wall1); // Přidá první zeď do seznamu zdí
        walls.add(wall2); // Přidá druhou zeď do seznamu zdí
        heartz = new Heartz(980, 1,  "Heartz.png"); // Inicializuje první srdce hráče
        heartz2 = new Heartz(980, 1, "Heartz2.png"); // Inicializuje druhé srdce hráče
        heartz3 = new Heartz(980, 1, "Heartz3.png"); // Inicializuje třetí srdce hráče
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Volání metody rodiče pro vykreslení komponent
        if (gameState == 2) { // Pokud je stav hry 2
            backGround.draw(g); // Kreslí pozadí
            player.draw(g); // Kreslí hráče
            for (Wall wall : walls) { // Pro každou zeď ve seznamu zdí
                //              if (wall.isActive()) {
                g.drawImage(wall.getImage(),wall.getX(),wall.getY(),this); // Vykresluje zdi
                //               }
            }
            for (Bullet rocket : rockets) { // Pro každou raketu ve seznamu raket
                g.drawImage(rocket.getImage(), rocket.getCoord().x, rocket.getCoord().y, this); // Vykresluje rakety
            }
            g.setFont(customFont); // Nastavuje vlastní font pro texty
            g.drawString("Time Alive: " + getSecondsPassed(), 960, 100); // Vykresluje čas, po který hráč přežil
            g.drawString("Health: " + player.getLives(), 970, 80); // Vykresluje zdraví hráče
            if (player.getLives() > 6) { // Pokud je zdraví hráče větší než 6
                g.drawImage(heartz.getImage(), heartz.getX(), heartz.getY(), null); // Vykresluje první srdce hráče
            } else if (player.getLives() > 3) { // Pokud je zdraví hráče větší než 3
                g.drawImage(heartz2.getImage(), heartz2.getX(), heartz2.getY(), null); // Vykresluje druhé srdce hráče
            } else if (player.getLives() > 0) { // Pokud je zdraví hráče větší než 0
                g.drawImage(heartz3.getImage(), heartz3.getX(), heartz3.getY(), null); // Vykresluje třetí srdce hráče
            }
        } else if (gameState == 3) { // Pokud je stav hry 3
            endGamePic.draw(g); // Kreslí koncový obrázek
        } else { // Pro všechny ostatní stavy hry
            startGamePic.draw(g); // Kreslí startovní obrázek
            g.setFont(customFont); // Nastavuje vlastní font pro texty
            g.setColor(Color.WHITE); // Nastavuje bílou barvu
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() { // Vytvoří nový časovač s periodou 1000 ms
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++; // Inkrementuje počet uplynulých sekund
            }
        });
        timer.start(); // Spustí časovač
    }

    private void stopTimer() {
        if (timer != null) { // Pokud je časovač nastaven na nenulovou hodnotu
            timer.stop(); // Zastaví časovač
        }
    }

    public int getSecondsPassed() {
        return secondsPassed; // Vrátí počet uplynulých sekund
    }

    public void changeGameState() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameState = 2; // Změní stav hry na 2 po kliknutí myší
            }
        });
    }


    public void update() {
        ArrayList<Bullet> rocketsToRemove = new ArrayList<>(); // Seznam raket k odstranění
        changeGameState(); // Změní stav hry

        controlledMove(); // Provádí řízený pohyb

        for (Bullet rocket : rockets) { // Pro každou raketu ve seznamu raket
            rocket.move(); // Pohne rakety
            if (player.isCollided(rocket.getRectangle()) && !player.dead) { // Pokud hráč koliduje s raketou a není mrtvý
                player.hit(); // Hráč je zasažen
                player.dead = true; // Nastaví hráče jako mrtvého
                rocketsToRemove.add(rocket); // Přidá raketu do seznamu k odstranění
            }
            if (player.dead) { // Pokud je hráč mrtvý
                player.timeToAlive -= 2; // Sníží čas naživu hráče o 2
            }
            if (player.dead && player.timeToAlive < 0) { // Pokud je hráč mrtvý a čas naživu je menší než 0
                player.dead = false; // Nastaví hráče jako živého
                player.timeToAlive = 1500; // Nastaví čas naživu na 1500
            }
        }
        rockets.removeAll(rocketsToRemove); // Odebere všechny rakety ze seznamu
        spawnInterval(); // Provede spawn nepřátel
        if (player.getLives() <= 0) { // Pokud je zdraví hráče menší nebo rovno 0
            gameState = 3; // Nastaví stav hry na 3 (konec hry)
        }
    }

    private void controlledMove() {
        int newX = player.getX(); // Nová pozice X hráče
        int newY = player.getY(); // Nová pozice Y hráče

        if (keyReader.upPressed) newY -= player.getSpeed(); // Pokud je stisknuta klávesa nahoru, posune hráče nahoru
        if (keyReader.downPressed) newY += player.getSpeed(); // Pokud je stisknuta klávesa dolů, posune hráče dolů
        if (keyReader.leftPressed) newX -= player.getSpeed(); // Pokud je stisknuta klávesa doleva, posune hráče doleva
        if (keyReader.rightPressed) newX += player.getSpeed(); // Pokud je stisknuta klávesa doprava, posune hráče doprava

        if (keyReader.rightPressed && keyReader.upPressed) { // Pokud jsou stisknuty klávesy doprava a nahoru současně
            newX += player.getSpeed() - 8; // Posune hráče doprava
            newY -= player.getSpeed() - 8; // Posune hráče nahoru
        }
        if (keyReader.leftPressed && keyReader.upPressed) { // Pokud jsou stisknuty klávesy doleva a nahoru současně
            newX -= player.getSpeed() - 8; // Posune hráče doleva
            newY -= player.getSpeed() - 8; // Posune hráče nahoru
        }
        if (keyReader.leftPressed && keyReader.downPressed) { // Pokud jsou stisknuty klávesy doleva a dolů současně
            newX -= player.getSpeed() - 8; // Posune hráče doleva
            newY += player.getSpeed() - 8; // Posune hráče dolů
        }
        if (keyReader.rightPressed && keyReader.downPressed) { // Pokud jsou stisknuty klávesy doprava a dolů současně
            newX += player.getSpeed() - 8; // Posune hráče doprava
            newY += player.getSpeed() - 8; // Posune hráče dolů
        }

        Rectangle newPlayerRectangle = new Rectangle(newX, newY, player.getWidth(), player.getHeight()); // Nový obdélník hráče

        for
        (Wall wall : walls) { // Pro každou zeď ve seznamu zdí
            Rectangle wallRectangle = wall.getRectangle(); // Získá obdélník zdi

            // Kontroluje horizontální kolize
            if (newPlayerRectangle.intersects(wallRectangle)) { // Pokud se nový obdélník hráče protíná s obdélníkem zdi
                if (keyReader.leftPressed || keyReader.rightPressed) { // Pokud je stisknuta klávesa doleva nebo doprava
                    if (keyReader.leftPressed) { // Pokud je stisknuta klávesa doleva
                        newX = Math.max(newX, wall.getX() + wall.getWidth()); // Nové X bude maximem z aktuálního X a X pozice zdi + její šířka
                    }
                    if (keyReader.rightPressed) { // Pokud je stisknuta klávesa doprava
                        newX = Math.min(newX, wall.getX() - player.getWidth()); // Nové X bude minimem z aktuálního X a X pozice zdi - šířka hráče
                    }
                }

                // Aktualizuje pozici hráče po horizontální úpravě
                newPlayerRectangle.setLocation(newX, player.getY());
            }

            // Kontroluje vertikální kolize
            if (newPlayerRectangle.intersects(wallRectangle)) { // Pokud se nový obdélník hráče protíná s obdélníkem zdi
                if (keyReader.upPressed || keyReader.downPressed) { // Pokud je stisknuta klávesa nahoru nebo dolů
                    if (keyReader.upPressed) { // Pokud je stisknuta klávesa nahoru
                        newY = Math.max(newY, wall.getY() + wall.getHeight()); // Nové Y bude maximem z aktuálního Y a Y pozice zdi + její výška
                    }
                    if (keyReader.downPressed) { // Pokud je stisknuta klávesa dolů
                        newY = Math.min(newY, wall.getY() - player.getHeight()); // Nové Y bude minimem z aktuálního Y a Y pozice zdi - výška hráče
                    }
                }

                // Aktualizuje pozici hráče po vertikální úpravě
                newPlayerRectangle.setLocation(player.getX(), newY);
            }
        }

        // Aktualizuje pozici hráče
        player.setX(newX);
        player.setY(newY);
    }

    private void spawnRocket() {
        Random rando = new Random(); // Vytvoří novou instanci generátoru náhodných čísel
        int randPick = rando.nextInt(4) + 1; // Generuje náhodné číslo od 0 do 3 a přičte 1
        Random random = new Random(); // Vytvoří novou instanci generátoru náhodných čísel
        for (int i = 0; i < randPick; i++) { // Pro každou raketu z náhodného výběru
            int randCorner = random.nextInt(4); // Náhodně vybere jedno z čísel od 0 do 3 (vybere náhodný roh)

            Point point = null; // Inicializuje bod na null
            switch (randCorner) { // Rozhoduje podle náhodného rohu

                case 0: // Pokud je vybrán roh 0
                    point = pointToEnemy(player.x, player.y, 0, 0, 6); // Nastaví bod směrem k hráči
                    rockets.add(new Bullet(0, 0, point.x, point.y, "Bullet.png")); // Přidá raketu do seznamu na pozici (0, 0)
                    break;

                case 1: // Pokud je vybrán roh 1
                    point = pointToEnemy(player.x, player.y, 1080, 0, 6); // Nastaví bod směrem k hráči
                    rockets.add(new Bullet(1080, 0, point.x, point.y, "Bullet.png")); // Přidá raketu do seznamu na pozici (1080, 0)
                    break;
                case 2: // Pokud je vybrán roh 2
                    point = pointToEnemy(player.x, player.y, 0, 720, 6); // Nastaví bod směrem k hráči
                    rockets.add(new Bullet(0, 720, point.x, point.y, "Bullet.png")); // Přidá raketu do seznamu na pozici (0, 720)
                    break;
                case 3: // Pokud je vybrán roh 3
                    point = pointToEnemy(player.x, player.y, 1080, 720, 6); // Nastaví bod směrem k hráči
                    rockets.add(new Bullet(1080, 720, point.x, point.y, "Bullet.png")); // Přidá raketu do seznamu na pozici (1080, 720)
                    break;

            }
        }
    }

    // Vypočítá bod směřující k nepřáteli
    public Point pointToEnemy(int enemyX, int enemyY, int coordX, int coordY, double speed) {
        double angle = Math.atan2(enemyY - coordY, enemyX - coordX); // Vypočítá úhel

        return new Point((int) (speed * Math.cos(angle)), (int) (speed * Math.sin(angle))); // Vrátí bod se souřadnicemi směrem k nepříteli
    }

    // Provádí interval spawnu raket
    public void spawnInterval() {
        startCount++; // Inkrementuje startovní počet
        if (startCount == spawnRate) { // Pokud je startovní počet roven spaw rate
            spawnRocket(); // Spustí spawn raket
            startCount = 0; // Resetuje startovní počet
        }
    }

    // Spustí herní vlákno
    public void startGameThread() {
        gameThread = new Thread(this); // Vytvoří nové herní vlákno
        gameThread.start(); // Spustí herní vlákno
    }

    @Override
    public void run() { // Implementace metody z rozhraní Runnable

        while (gameThread != null) { // Dokud není herní vlákno null
            currentTime = System.nanoTime(); // Získá aktuální čas

            delta += (currentTime - lastTime) / drawInterval; // Přičte k deltě rozdíl mezi aktuálním a posledním časem vyděleným intervaly vykreslování
            lastTime = currentTime; // Aktualizuje poslední čas

            if (delta >= 1) { // Pokud je delta větší nebo rovna 1
                update(); // Aktualizuje hru
                repaint(); // Překreslí herní plátno
                delta--; // Sníží deltě hodnotu o 1
            }
        }
    }

    // Resetuje hru
    public void resetGame() {
        gameState = 1; // Nastaví stav hry na začátek
        gameStarted = false; // Resetuje flag, který označuje, zda hra začala
        player = new Player(this, keyReader, 500, 500, "Player.gif"); // Vytvoří novou instanci hráče
        enemies.clear(); // Vyčistí seznam nepřátel
        walls.clear(); // Vyčistí seznam zdí
        rockets.clear(); // Vyčistí seznam raket
        secondsPassed = 0; // Resetuje počet sekund, které uplynuly
        startCount = 0; // Resetuje startovní počet
        initialize(); // Inicializuje herní objekty
        repaint(); // Překreslí herní plátno
        stopTimer(); // Zastaví časovač
    }
}
