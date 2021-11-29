//Semigroup assotiativity
//Monoid = Semigroup + Identity

type Semigroup<T> = (x: T) => {
  x: T;
  concat: (other: T) => Semigroup<T>;
};

type Monoid<T> = Semigroup<T> & {
  empty: () => T;
};

const Sum: Semigroup<number> = (x: number) => ({
  x,
  concat: (other: number) => Sum(x + other),
});
