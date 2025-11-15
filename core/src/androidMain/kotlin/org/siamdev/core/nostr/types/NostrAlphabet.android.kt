/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDev by SiamDharmar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.core.nostr.types

import rust.nostr.sdk.Alphabet as NativeAlphabet

actual enum class NostrAlphabet(internal val native: NativeAlphabet) {
    A(NativeAlphabet.A),
    B(NativeAlphabet.B),
    C(NativeAlphabet.C),
    D(NativeAlphabet.D),
    E(NativeAlphabet.E),
    F(NativeAlphabet.F),
    G(NativeAlphabet.G),
    H(NativeAlphabet.H),
    I(NativeAlphabet.I),
    J(NativeAlphabet.J),
    K(NativeAlphabet.K),
    L(NativeAlphabet.L),
    M(NativeAlphabet.M),
    N(NativeAlphabet.N),
    O(NativeAlphabet.O),
    P(NativeAlphabet.P),
    Q(NativeAlphabet.Q),
    R(NativeAlphabet.R),
    S(NativeAlphabet.S),
    T(NativeAlphabet.T),
    U(NativeAlphabet.U),
    V(NativeAlphabet.V),
    W(NativeAlphabet.W),
    X(NativeAlphabet.X),
    Y(NativeAlphabet.Y),
    Z(NativeAlphabet.Z);
}

