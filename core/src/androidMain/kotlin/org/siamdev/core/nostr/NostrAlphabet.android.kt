package org.siamdev.core.nostr

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
