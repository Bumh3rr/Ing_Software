package bumh3r.model.other;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFull {
    public static final String[] WEEKS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    public static final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    public static String getDateTimeFull(LocalDateTime date) {
        String value = "dd 'de' MMMM 'del' yyyy / hh:mm:ss a";
        return date.format(DateTimeFormatter.ofPattern(value)).concat(" / " + WEEKS[date.getDayOfWeek().getValue() - 1]);
    }

    public static String getDateFullWithTime(LocalDateTime date) {
        return date.format(DateTimeFormatter
                .ofPattern("dd 'de' '" + MONTHS[date.getMonth().getValue() - 1] + "' 'del' yyyy - hh:mm a"));
    }

    public static String getDateFull(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy")).concat(" / " + WEEKS[date.getDayOfWeek().getValue() - 1]);
    }

    public static String getDateOnly(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy"));
    }

    public static String getWeekOnly(LocalDateTime date) {
        return WEEKS[date.getDayOfWeek().getValue() - 1];
    }

    public static String getHourOnly(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static String getDateFull(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy"));
    }


}
