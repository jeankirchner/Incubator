use rand::Rng;
use std::cmp::Ordering;
use std::io;

fn simple_loop_guess() {
    loop {
        println!("Type a number:");

        let secret = rand::thread_rng().gen_range(1..101);

        println!("The secret number is: {}", secret);
        let mut guess = String::new();

        io::stdin()
            .read_line(&mut guess)
            .expect("Failed to read line");

        let guess: u32 = match guess.trim().parse() {
            Ok(num) => num,
            Err(_) => continue,
        };

        match guess.cmp(&secret) {
            Ordering::Less => println!("Too small!"),
            Ordering::Greater => println!("Too big!"),
            Ordering::Equal => {
                println!("You Win!");
                break;
            }
        }

        println!("You guessed: {}", guess);
    }
}

fn common_concepts() {
    let mut x = 5;
    println!("x: {}", x);
    x = 6;
    println!("x: {}", x);

    const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;

    println!("hours in secs: {}", THREE_HOURS_IN_SECONDS);

}

fn main() {
    common_concepts();
}
