package CreditCalculator;

public class CreditCalculator {
    public static void main(String[] args) {
        // Получаем параметры командной строки
        String type = System.getProperty("type");
        String principalStr = System.getProperty("principal");
        String periodsStr = System.getProperty("periods");
        String interestStr = System.getProperty("interest");
        String paymentStr = System.getProperty("payment");

        // --- ВАЛИДАЦИЯ ПАРАМЕТРОВ (как описано в задании) ---
        boolean isError = false;

        // 1. Процент (--interest) обязателен всегда
        if (interestStr == null) { isError = true; }

        // 2. Проверка типа платежа
        if (type == null || (!type.equals("diff") && !type.equals("annuity"))) { isError = true; }

        // 3. Для diff нельзя указывать payment
        if (type != null && type.equals("diff") && paymentStr != null) { isError = true; }

        // Парсим числа для дальнейшей проверки (если строки не null)
        double principal = (principalStr != null) ? Double.parseDouble(principalStr) : -1;
        int periods = (periodsStr != null) ? Integer.parseInt(periodsStr) : -1;
        double annualInterest = (interestStr != null) ? Double.parseDouble(interestStr) : -1;
        double payment = (paymentStr != null) ? Double.parseDouble(paymentStr) : -1;

        // 4. Запрет отрицательных значений
        if ( (principalStr != null && principal < 0) ||
                (periodsStr != null && periods < 0) ||
                (interestStr != null && annualInterest < 0) ||
                (paymentStr != null && payment < 0) ) {
            isError = true;
        }

        // Подсчет количества введенных параметров (из 4 числовых)
        int paramsCount = 0;
        if (principalStr != null) paramsCount++;
        if (periodsStr != null) paramsCount++;
        if (interestStr != null) paramsCount++;
        if (paymentStr != null) paramsCount++;

        // 5. Должно быть как минимум 4 параметра всего (type + 3 числовых)
        // Но лучше проверять, что не хватает ровно одного искомого значения для типа
        if (type != null && type.equals("annuity") && paramsCount < 3) { isError = true; } // ищем 1 из 3-х
        if (type != null && type.equals("diff") && paramsCount < 3) { isError = true; } // ищем diff платежи (даны P, n, %)

        if (isError) {
            System.out.println("Incorrect parameters");
            return; // Выходим из программы
        }

        // --- ЛОГИКА РАСЧЕТОВ ---
        double i = annualInterest / (12 * 100);

        if (type.equals("diff")) {
            // --- РАСЧЕТ ДИФФЕРЕНЦИРОВАННЫХ ПЛАТЕЖЕЙ ---
            // Даны: P, n, %
            if (principalStr == null || periodsStr == null) {
                System.out.println("Incorrect parameters"); return;
            }

            long totalPaid = 0;
            for (int m = 1; m <= periods; m++) {
                // Формула: Dm = (P/n) + i * (P - (P*(m-1))/n)
                double currentPayment = (principal / periods) + i * (principal - (principal * (m - 1)) / periods);
                // Округляем каждый платеж вверх
                int roundedCurrentPayment = (int) Math.ceil(currentPayment);
                System.out.println("Month " + m + ": payment is " + roundedCurrentPayment);
                totalPaid += roundedCurrentPayment;
            }

            System.out.println("\nOverpayment = " + (totalPaid - (long)principal));

        } else if (type.equals("annuity")) {
            // --- АННУИТЕТНЫЕ ПЛАТЕЖИ (логика из Этапа 3) ---

            if (principalStr != null && paymentStr != null) {
                // Ищем n
                double x = payment / (payment - i * principal);
                double nDouble = Math.log(x) / Math.log(1 + i);
                int totalMonths = (int) Math.ceil(nDouble);

                printTime(totalMonths);
                long overpayment = (long) (totalMonths * payment - principal);
                System.out.println("Overpayment = " + overpayment);
            } else if (principalStr != null && periodsStr != null) {
                // Ищем A (платеж)
                double powerTerm = Math.pow(1 + i, periods);
                double annuityPayment = principal * (i * powerTerm) / (powerTerm - 1);
                int roundedPayment = (int) Math.ceil(annuityPayment);
                System.out.println("Your annuity payment = " + roundedPayment + "!");

                long overpayment = (long) (roundedPayment * periods - principal);
                System.out.println("Overpayment = " + overpayment);

            } else if (paymentStr != null && periodsStr != null) {
                // Ищем P (основную сумму)
                double powerTerm = Math.pow(1 + i, periods);
                double principalCalc = payment / ( (i * powerTerm) / (powerTerm - 1) );

                System.out.println("Your loan principal = " + (int)Math.round(principalCalc) + "!");

                long overpayment = (long) (payment * periods - principalCalc);
                System.out.println("Overpayment = " + overpayment);
            } else {
                System.out.println("Incorrect parameters");
            }
        }
    }

    // Вспомогательный метод для красивого вывода времени (из этапа 3)
    private static void printTime(int totalMonths) {
        int years = totalMonths / 12;
        int months = totalMonths % 12;
        String resultTime = "";
        if (years > 0) resultTime += years + (years == 1 ? " year" : " years");
        if (years > 0 && months > 0) resultTime += " and ";
        if (months > 0) resultTime += months + (months == 1 ? " month" : " months");
        System.out.println("It will take " + resultTime + " to repay this loan!");
    }
}