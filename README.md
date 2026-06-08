# ♟️ پروژه شطرنج — مستندات کامل

> **نویسندگان:** پویان توانا شیروان — پارسا امامی آرندی  
> **مرحله:** فاز ۱ (پیاده‌سازی پایه) + فاز ۲ (ویژگی‌های پیشرفته)  
> **زبان برنامه‌نویسی:** Java  
> **نوع پروژه:** بازی شطرنج دو نفره در محیط ترمینال

---

## فهرست مطالب

1. [معرفی پروژه](#معرفی-پروژه)
2. [نحوه اجرا](#نحوه-اجرا)
3. [ساختار کلی پروژه](#ساختار-کلی-پروژه)
4. [توضیح فایل‌ها](#توضیح-فایل‌ها)
   - [Piece.java](#piecejava--کلاس-پایه-انتزاعی)
   - [Board.java](#boardjava--صفحه-بازی)
   - [ChessGame.java](#chessgamejava--کنترلر-بازی)
   - [Pawn.java](#pawnjava--پیاده)
   - [Rook.java](#rookjava--رخ)
   - [Knight.java](#knightjava--اسب)
   - [Bishop.java](#bishopjava--فیل)
   - [Queen.java](#queenjava--وزیر)
   - [King.java](#kingjava--شاه)
   - [ProjectPhase1.java](#projectphase1java--نقطه-ورود)
5. [نحوه تعامل کلاس‌ها با یکدیگر](#نحوه-تعامل-کلاس‌ها-با-یکدیگر)
6. [الگوریتم‌های مهم](#الگوریتم‌های-مهم)
7. [فاز ۲ — ویژگی‌های پیشرفته](#فاز-۲--ویژگی‌های-پیشرفته)
8. [محدودیت‌ها و کارهای آینده](#محدودیت‌ها-و-کارهای-آینده)

---

## معرفی پروژه

این پروژه یک بازی شطرنج دو نفره است که در محیط ترمینال اجرا می‌شود. بازی با استفاده از اصول **برنامه‌نویسی شیءگرا (OOP)** پیاده‌سازی شده و از سه اصل اصلی بهره می‌برد:

- **وراثت (Inheritance):** تمام مهره‌ها از کلاس پایه `Piece` ارث می‌برند.
- **چندریختی (Polymorphism):** متد `canMove()` در هر مهره متفاوت رفتار می‌کند.
- **کپسوله‌سازی (Encapsulation):** داده‌های داخلی هر کلاس از طریق متدهای عمومی در دسترس هستند.

---

## نحوه اجرا

```bash
# کامپایل همه فایل‌ها
javac *.java

# اجرای برنامه
java ProjectPhase1
```

**نحوه وارد کردن حرکت:**

حرکت‌ها به فرمت شطرنجی استاندارد وارد می‌شوند. مثلاً برای حرکت پیاده از `e2` به `e4`:

```
e2 e4
```

---

## ساختار کلی پروژه

```
ProjectPhase1        ← نقطه ورود (main)
      ↓
  ChessGame          ← حلقه بازی و مدیریت ورودی
      ↓
    Board            ← صفحه ۸×۸ و توابع کمکی
      ↓
    Piece            ← کلاس پایه انتزاعی
   /  |  \  \   \   \
Pawn Rook Bishop Knight Queen King
```

**فایل‌های پروژه:**

| فایل | نقش |
|------|-----|
| `Piece.java` | کلاس پایه انتزاعی برای همه مهره‌ها |
| `Board.java` | مدیریت صفحه و توابع مسیر |
| `ChessGame.java` | حلقه اصلی بازی |
| `Pawn.java` | منطق حرکت پیاده |
| `Rook.java` | منطق حرکت رخ |
| `Knight.java` | منطق حرکت اسب |
| `Bishop.java` | منطق حرکت فیل |
| `Queen.java` | منطق حرکت وزیر |
| `King.java` | منطق حرکت شاه |
| `ProjectPhase1.java` | نقطه ورود برنامه |

---

## توضیح فایل‌ها

---

### `Piece.java` — کلاس پایه انتزاعی

این کلاس **پایه و اساس** تمام مهره‌هاست. هر مهره‌ای در بازی از این کلاس ارث می‌برد.

```java
abstract class Piece {
    protected boolean white;      // رنگ مهره (سفید یا سیاه)

    public Piece(boolean white) { ... }
    public boolean isWhite() { ... }
    public abstract String getSymbol();    // نماد متنی مهره
    public abstract boolean canMove(...); // بررسی اعتبار حرکت
}
```

**نکات مهم:**

- فیلد `white` با سطح دسترسی `protected` تعریف شده تا زیرکلاس‌ها مستقیماً به آن دسترسی داشته باشند.
- متد `getSymbol()` در مهره‌های سفید حرف بزرگ و در مهره‌های سیاه حرف کوچک برمی‌گرداند. مثلاً `R` برای رخ سفید و `r` برای رخ سیاه.
- متد `canMove()` **انتزاعی** است یعنی هر زیرکلاس باید منطق حرکت خودش را پیاده‌سازی کند.

---

### `Board.java` — صفحه بازی

این کلاس **صفحه ۸×۸** بازی را مدیریت می‌کند و توابع کمکی برای بررسی مسیر حرکت فراهم می‌آورد.

```java
class Board {
    private Piece[][] board;  // آرایه دوبعدی ۸×۸
}
```

#### متدهای مهم:

**`initializeBoard()`** — چیدمان اولیه مهره‌ها

مهره‌های سفید در ردیف‌های ۶ و ۷ (پایین) و مهره‌های سیاه در ردیف‌های ۰ و ۱ (بالا) قرار می‌گیرند:

```
ردیف ۰: مهره‌های پشتی سیاه  (r n b q k b n r)
ردیف ۱: پیاده‌های سیاه
...
ردیف ۶: پیاده‌های سفید
ردیف ۷: مهره‌های پشتی سفید  (R N B Q K B N R)
```

**`movePiece()`** — اجرای حرکت

```java
board[toRow][toCol] = board[fromRow][fromCol];
board[fromRow][fromCol] = null;
```

مهره مقصد جایگزین می‌شود (خوردن مهره دشمن به صورت خودکار انجام می‌شود) و خانه مبدأ خالی می‌شود.

**`isPathClearStraight()`** — ⭐ الگوریتم بررسی مسیر مستقیم

برای رخ و وزیر استفاده می‌شود. تمام خانه‌های بین مبدأ و مقصد را بررسی می‌کند:

```java
int step = (toCol > fromCol) ? 1 : -1;
for (int c = fromCol + step; c != toCol; c += step) {
    if (board[fromRow][c] != null) return false;
}
```

**`isPathClearDiagonal()`** — ⭐ الگوریتم بررسی مسیر قطری

برای فیل و وزیر استفاده می‌شود. همزمان ردیف و ستون را جابجا می‌کند:

```java
int rowStep = (toRow > fromRow) ? 1 : -1;
int colStep = (toCol > fromCol) ? 1 : -1;
int r = fromRow + rowStep, c = fromCol + colStep;
while (r != toRow && c != toCol) {
    if (board[r][c] != null) return false;
    r += rowStep; c += colStep;
}
```

**`isInside()`** — بررسی محدوده صفحه

```java
return row >= 0 && row < 8 && col >= 0 && col < 8;
```

---

### `ChessGame.java` — کنترلر بازی

این کلاس **حلقه اصلی بازی** را مدیریت می‌کند. یک `Board` دارد، نوبت بازیکنان را ردیابی می‌کند و ورودی کاربر را پردازش می‌کند.

```java
class ChessGame {
    private Board board;
    private boolean whiteTurn;   // نوبت چه کسی است
    private Scanner scanner;
}
```

#### ⭐ متد `start()` — الگوریتم حلقه اصلی بازی

یک حلقه بی‌نهایت که هر نوبت مراحل زیر را طی می‌کند:

1. **نمایش** صفحه
2. **دریافت** ورودی از بازیکن (مثلاً `e2 e4`)
3. **تبدیل** نماد شطرنجی به شماره ردیف/ستون:
   ```java
   int fromRow = 8 - Character.getNumericValue(from.charAt(1)); // 'e2' → ردیف ۶
   int fromCol = from.charAt(0) - 'a';                          // 'e'  → ستون ۴
   ```
4. **اعتبارسنجی** به ترتیب:
   - فرمت ورودی درست است؟
   - خانه‌ها داخل صفحه هستند؟
   - مهره‌ای در خانه مبدأ هست؟
   - مهره متعلق به بازیکن فعلی است؟
   - `canMove()` مقدار `true` برمی‌گرداند؟
   - مقصد توسط مهره دوستانه اشغال نشده؟
5. **اجرای** حرکت
6. **تغییر** نوبت

---

### `Pawn.java` — پیاده

پیاده پیچیده‌ترین منطق حرکت را دارد چون قوانین نامتقارن دارد.

#### ⭐ منطق `canMove()` — الگوریتم حرکت پیاده

**حالت ۱ — یک خانه به جلو:**
```java
fromCol == toCol && toRow == fromRow + direction && board.getPiece(toRow, toCol) == null
```
مقصد باید خالی باشد — پیاده نمی‌تواند مستقیم بخورد.

**حالت ۲ — دو خانه از خانه شروع:**
```java
white && fromRow == 6 && toRow == 4
    && board.getPiece(5, toCol) == null
    && board.getPiece(4, toCol) == null
```
هر دو خانه میانی و مقصد باید خالی باشند.

**حالت ۳ — خوردن قطری:**
```java
Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction
    && target != null && target.isWhite() != white
```
یک خانه قطری به جلو، فقط وقتی مهره دشمن آنجاست.

---

### `Rook.java` — رخ

```java
if (fromRow == toRow || fromCol == toCol) {
    return board.isPathClearStraight(fromRow, fromCol, toRow, toCol);
}
return false;
```

فقط در همان ردیف یا ستون حرکت می‌کند. بررسی آزاد بودن مسیر را به `Board` واگذار می‌کند.

---

### `Knight.java` — اسب

```java
int rowDiff = Math.abs(toRow - fromRow);
int colDiff = Math.abs(toCol - fromCol);
return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
```

حرکت به شکل L. اسب از روی مهره‌های دیگر می‌پرد پس **هیچ بررسی مسیری لازم ندارد**.

---

### `Bishop.java` — فیل

```java
int rowDiff = Math.abs(toRow - fromRow);
int colDiff = Math.abs(toCol - fromCol);
if (rowDiff == colDiff) {
    return board.isPathClearDiagonal(...);
}
```

شرط `rowDiff == colDiff` تعریف ریاضی حرکت قطری ۴۵ درجه است.

---

### `Queen.java` — وزیر

وزیر ترکیبی از رخ و فیل است:

```java
if (fromRow == toRow || fromCol == toCol)
    return board.isPathClearStraight(...);
if (rowDiff == colDiff)
    return board.isPathClearDiagonal(...);
return false;
```

از همان متدهای `Board` که رخ و فیل استفاده می‌کنند بهره می‌برد — نمونه خوبی از استفاده مجدد کد.

---

### `King.java` — شاه

```java
return rowDiff <= 1 && colDiff <= 1;
```

یک خانه در هر جهت. بررسی کیش نیاز به منطق اضافی در `ChessGame` دارد.

---

### `ProjectPhase1.java` — نقطه ورود

```java
public static void main(String[] args) {
    ChessGame game = new ChessGame();
    game.start();
}
```

ساده و تمیز. فقط بازی را ایجاد و شروع می‌کند.

---

## نحوه تعامل کلاس‌ها با یکدیگر

```
ProjectPhase1
    │  می‌سازد
    ▼
ChessGame ──── دارد ────► Board ──── نگه می‌دارد ────► Piece[][]
    │                       │
    │  فراخوانی می‌کند      │  توابع کمکی برای زیرکلاس‌ها:
    │  board.printBoard()   │  • isPathClearStraight()
    │  board.getPiece()     │  • isPathClearDiagonal()
    │  board.isInside()     │  • getPiece()
    │  board.movePiece()    │  • isInside()
    │
    │  piece.canMove(board, ...) فراخوانی چندریختی
    │        ↓
    ├──► Pawn.canMove()    ──► board.getPiece()
    ├──► Rook.canMove()    ──► board.isPathClearStraight()
    ├──► Bishop.canMove()  ──► board.isPathClearDiagonal()
    ├──► Knight.canMove()  (نیازی به فراخوانی Board ندارد)
    ├──► Queen.canMove()   ──► isPathClearStraight() یا isPathClearDiagonal()
    └──► King.canMove()    (نیازی به فراخوانی Board ندارد)
```

**نکته معماری کلیدی:** `ChessGame` ارجاع `Board` را به هر فراخوانی `canMove()` پاس می‌دهد. این به هر مهره اجازه می‌دهد وضعیت صفحه را بررسی کند بدون اینکه خودش ارجاعی به صفحه نگه دارد.

---

## الگوریتم‌های مهم

### ۱. تبدیل نماد شطرنجی به مختصات آرایه

```java
// 'e2' → ردیف ۶، ستون ۴
int row = 8 - Character.getNumericValue(notation.charAt(1));
int col = notation.charAt(0) - 'a';
```

| نماد | ردیف | ستون |
|------|------|------|
| a1 | 7 | 0 |
| e2 | 6 | 4 |
| h8 | 0 | 7 |

### ۲. بررسی مسیر مستقیم (رخ و وزیر)

برای هر حرکت افقی یا عمودی، تمام خانه‌های بینابین بررسی می‌شوند. اگر هر خانه‌ای خالی نباشد، حرکت غیرمجاز است.

### ۳. بررسی مسیر قطری (فیل و وزیر)

با استفاده از دو متغیر گام (`rowStep` و `colStep`) که هر دو `+1` یا `-1` هستند، خانه‌های قطری بینابین بررسی می‌شوند.

### ۴. حرکت اسب (L شکل)

```java
(rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
```

فقط یک بررسی ریاضی ساده — نیازی به بررسی مسیر نیست.

---

## فاز ۲ — ویژگی‌های پیشرفته

در فاز ۲ چهار ویژگی اضافه شدند:

---

### ۱. ⚔️ کیش و کیش‌ومات (Check & Checkmate)

**در `Board.java`** متد `findKing()` اضافه شد تا موقعیت شاه را پیدا کند.

**در `ChessGame.java`** دو متد اضافه شدند:

- **`isInCheck(boolean white)`:** بررسی می‌کند آیا شاه بازیکن مشخص شده توسط هر مهره دشمن تهدید می‌شود.
- **`hasAnyLegalMove(boolean white)`:** بررسی می‌کند آیا بازیکن هیچ حرکت قانونی دارد — برای تشخیص کیش‌ومات و بازی‌مرده.

اگر بعد از یک حرکت شاه خودی در کیش باشد، آن حرکت لغو می‌شود.

---

### ۲. 🏰 قلعه (Castling)

**در `Rook.java` و `King.java`** فیلد `hasMoved` اضافه شد تا ردیابی شود آیا رخ یا شاه قبلاً حرکت کرده‌اند.

شرایط قلعه:
- شاه و رخ هیچ‌کدام قبلاً حرکت نکرده باشند
- خانه‌های بین آن‌ها خالی باشند
- شاه از میان کیش رد نشود

**قلعه کوچک:** شاه دو خانه به سمت رخ h حرکت می‌کند.  
**قلعه بزرگ:** شاه دو خانه به سمت رخ a حرکت می‌کند.

---

### ۳. 🎯 آنپاسان (En Passant)

**در `Pawn.java`** فیلد `justDoubleMoved` اضافه شد.

وقتی پیاده‌ای دو خانه حرکت می‌کند، این فلگ `true` می‌شود. پیاده دشمن در نوبت بعدی می‌تواند آن پیاده را به صورت مورب بزند حتی اگر خانه مقصد خالی باشد. پیاده خورده شده از صفحه برداشته می‌شود.

---

### ۴. 👑 ارتقای پیاده (Pawn Promotion)

وقتی یک پیاده به آخرین ردیف برسد (ردیف ۰ برای سفید، ردیف ۷ برای سیاه)، به طور خودکار به **وزیر** تبدیل می‌شود.

> **نکته:** در فاز ۳ می‌توان به بازیکن اجازه داد نوع ارتقا را خودش انتخاب کند (رخ، فیل، اسب یا وزیر).

---

## محدودیت‌ها و کارهای آینده

| ویژگی | وضعیت |
|-------|-------|
| کیش و کیش‌ومات | ✅ فاز ۲ |
| قلعه | ✅ فاز ۲ |
| آنپاسان | ✅ فاز ۲ |
| ارتقای پیاده | ✅ فاز ۲ (فقط به وزیر) |
| نمادهای یونیکد شطرنج | ❌ مشکل هم‌ترازی در ترمینال |
| انتخاب نوع ارتقا | ❌ فاز ۳ |
| رابط گرافیکی | ❌ فاز ۳ |
| بازی آنلاین | ❌ فاز آینده |
