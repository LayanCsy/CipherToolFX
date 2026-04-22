# 🔐 CipherToolFX
**CYB270 – Cryptography Group Project | Taibah University**

A JavaFX desktop application that allows users to encrypt and decrypt text and text files using three classical and modern encryption algorithms: **Atbash**, **Affine**, and **DES**.

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [How to Use](#how-to-use)
- [Cipher Details](#cipher-details)
  - [Affine Cipher](#-affine-cipher)
  - [Atbash Cipher](#-atbash-cipher)
  - [DES Cipher](#-des-data-encryption-standard)
- [Troubleshooting](#troubleshooting)
- [Team](#team)
- [References](#references)

---

## 📖 Overview

This tool was built as part of the CYB270 Cryptography course at Taibah University. It bridges mathematical foundations (Affine Cipher), historical encryption techniques (Atbash Cipher), and modern standards (DES) into a single educational tool.

The GUI was built using **JavaFX**, and no additional setup beyond the JDK is required to run it.

---

## ✨ Features

- 🔁 **Atbash Cipher** — Historical mirror-alphabet substitution cipher
- 🔢 **Affine Cipher** — Mathematical substitution cipher using keys `a` and `b`
- 🔒 **DES** — Symmetric block cipher using an 8-character secret key
- 📂 **File Input** — Load `.txt` files directly via file path
- ⌨️ **Manual Input** — Type or paste text directly into the GUI
- 🖥️ **JavaFX GUI** — Clean, intuitive graphical interface

---

## 🚀 Getting Started

### Prerequisites

- Java **JDK 11+**
- JavaFX SDK (if not bundled with your JDK)
- An IDE such as **IntelliJ IDEA** or **NetBeans**

### Running the App

**Option 1 — Using an IDE (Recommended):**
1. Clone or download this repository
2. Open the project in IntelliJ IDEA or NetBeans
3. Add JavaFX libraries to your module path if needed
4. Compile and run `CipherToolFX.java`

**Option 2 — Command Line:**
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls CipherToolFX.java
java  --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls CipherToolFX
```

> No additional installation or configuration is required beyond the JDK.

---

## 🧭 How to Use

1. **Select a cipher** from the `Cipher` dropdown (Atbash, Affine, or DES)
2. *(Optional)* Enter a **file path** to a `.txt` file to load text from it
3. Fill in the required **keys**:
   - **Affine** → `Key a` (must be coprime with 26) and `Key b` (any integer)
   - **DES** → `Key` (exactly 8 characters)
   - **Atbash** → No key needed
4. Select **Encrypt** or **Decrypt** using the radio buttons
5. Type or paste input text in the `Input Text` area *(or rely on the loaded file)*
6. Click **Process** — the result appears in the `Output` area
7. Click **Clear** to reset both input and output areas

---

## 🔬 Cipher Details

### 🔢 Affine Cipher

A monoalphabetic substitution cipher where each letter is mapped to a number, transformed using a mathematical formula, and converted back to a letter.

**Encryption formula:**
```
E(x) = (a × x + b) mod 26
```

**Decryption formula:**
```
D(x) = a⁻¹ × (x - b) mod 26
```

**Example — Encrypting `CYBER` with a=17, b=20:**

| Original | x  | ax+b % 26 | Encrypted |
|----------|----|-----------|-----------|
| C        | 2  | 2         | C         |
| Y        | 24 | 12        | M         |
| B        | 1  | 11        | L         |
| E        | 4  | 10        | K         |
| R        | 17 | 23        | X         |

> `a` must be **coprime with 26**. Valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25

---

### 🔁 Atbash Cipher

One of the oldest known ciphers — it simply reverses the alphabet. `A ↔ Z`, `B ↔ Y`, and so on. It is symmetric, meaning encryption and decryption use the exact same operation.

```
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
Z Y X W V U T S R Q P O N M L K J I H G F E D C B A
```

**Formula:**
- Uppercase: `Encrypted = 'Z' − (Letter − 'A')`
- Lowercase: `Encrypted = 'z' − (Letter − 'a')`

> No key is required. Atbash is a special case of the Affine cipher where `a = b = m - 1`.

---

### 🔒 DES (Data Encryption Standard)

A symmetric-key block cipher developed in the early 1970s. DES processes data in 64-bit blocks through 16 encryption rounds using a 56-bit key (entered as 8 ASCII characters).

**How it works:**
1. Input: 64-bit plaintext block + 8-character key
2. **Initial Permutation (IP):** Rearranges the bits using a fixed table
3. **Round-Key Generator:** Produces 16 × 48-bit subkeys from the 56-bit key
4. **16 Rounds**, each applying: Expansion → Key Mixing (XOR) → Substitution (S-boxes) → Permutation (P-box)
5. **Final Permutation (FP):** Produces the 64-bit ciphertext

**Implementation details:**
- Mode: `ECB` (Electronic Codebook)
- Padding: `PKCS5Padding`
- Output: **Base64-encoded** string
- Uses Java's built-in `javax.crypto` library

> ⚠️ DES is considered insecure for modern use due to its short key length. It is included here for **educational purposes only**.

---

## 🛠️ Troubleshooting

| Problem | Cause | Solution |
|---------|-------|----------|
| Invalid key for Affine | `a` is not coprime with 26 | Use valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25 |
| Invalid key for DES | Key is not exactly 8 characters | Ensure the key is exactly 8 characters long |
| File not found | Incorrect file path | Use the full absolute path and verify the file exists |
| No output appears | No operation selected or empty input | Select Encrypt or Decrypt and ensure text is provided |
| DES decryption fails | Input is not Base64 | Only decrypt data that was previously encrypted with this tool |
| Unexpected crash | Missing fields or locked file | Fill all required fields and ensure the file is not open elsewhere |

---

## 👥 Team

**Group F3 — Taibah University, Cybersecurity Department**
Supervised by: **Reem Kateb**

| ID      | Name                   | Contribution  |
|---------|------------------------|---------------|
| 4450974 | Dhay Nasser Al-Harbi   | Affine Cipher |
| 4454418 | Layan Faisal Al-Aboudi | DES Cipher    |
| 4451074 | Leen Ahmed Al-Oufi     | —             |
| 4453139 | Roaa Al-Juhani         | Atbash Cipher |
| 4450562 | Yara Al-Harbi          | —             |

---

## 📚 References

1. GeeksforGeeks — [Implementation of Affine Cipher](https://www.geeksforgeeks.org/implementation-affine-cipher/)
2. Crypto Corner — [Affine Cipher Interactive Tool](https://crypto.interactivemaths.com/affine-cipher.html)
3. Boxentriq — [Atbash Cipher Online Tool](https://www.boxentriq.com/codebreaking/atbash-cipher)
4. Dcode — [Atbash Cipher Decoder](https://www.dcode.fr/atbash-cipher)
5. B. Schneier, *Applied Cryptography*, 2nd ed., John Wiley & Sons, 2015
6. NIST — [Data Encryption Standard (DES)](https://csrc.nist.gov/publications/detail/fips/46-3/final)
7. W. Stallings, *Cryptography and Network Security*, 7th ed., Pearson Education, 2017

---

> 📌 *Second Semester 2025–2026 | CYB270 Cryptography | Taibah University*
