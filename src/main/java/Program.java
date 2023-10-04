import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Program {
    public static void main(String[] args) throws IOException {

        // Начальная инициализация
        Params params = new Params();
        File dir = null;

        Scanner in = new Scanner(System.in);
        // Цикл ввода с клавиатуры
        do {
            params.clearParams();
            // Запрос ввода команды

            System.out.println();
            System.out.println("Для вызова справки введите команду help.");
            System.out.println("Введите команду:");
            String str = in.nextLine();

            // Парсинг и проверка введённой команды
            parseLine(str, params);
            // определяем объект для каталога
            if (params.getErrorOrHelpMessage() == null) {
                dir = new File(params.getPath());
                if (!dir.isDirectory()) {
                    params.setErrorOrHelpMessage("Файл " + params.getPath() + " не является директорией");
                }
            }

            if (params.getErrorOrHelpMessage() != null) {
                System.out.println(params.getErrorOrHelpMessage());
            }

        } while (params.getErrorOrHelpMessage() != null);
        in.close();

        // Создание MAP для хранения статистики по файлам
        Map<String, Info> extInfo = new HashMap<>();

        // Разбираем параметры --include-ext и --exclude-ext на массивы
        List<String> incFilter;
        if (params.getIncludeFilter() != null) {
            incFilter = Arrays.asList(params.getIncludeFilter().split(",(?! )"));
        } else {
            incFilter = null;
        }

        List<String> excFilter;
        if (params.getExcludeFilter() != null) {
            excFilter = Arrays.asList(params.getExcludeFilter().split(",(?! )"));
        } else {
            excFilter = null;
        }

        // Запуск сбора статистики
        getInfo(extInfo, dir, params.getRecursively(), params.getDepth(), incFilter, excFilter);

        // Вывод результатов
        System.out.println("Собранная статистика: ");
        if (params.getOutput() != null && params.getOutput().contentEquals("json")) {
            JSONObject json = new JSONObject(extInfo);
            System.out.println(json);
        } else {
            Set<String> keySet = extInfo.keySet();
            for (String key : keySet) {
                System.out.println(key + extInfo.get(key).toString());
            }
        }
    }
    private static void getInfo(Map<String, Info> map,
                                File dir,
                                Boolean recursively,
                                Integer depth,
                                List<String> incFilter,
                                List<String> excFilter) throws IOException {

        if (depth == 0) {
            return;
        }

        // Получаем все вложенные файлы в каталоге
        if (dir.listFiles() != null) {
            for (File item : dir.listFiles()) {
                if (item.isDirectory()) {
                    if (recursively != null) {
                        // Запоминаем текущую глубину
                        Integer curDepth = depth;
                        getInfo(map, item, recursively, --curDepth, incFilter, excFilter);
                    }
                } else {
                    saveInfo(map, item, incFilter, excFilter);
                }
            }
        }
    }

    private static void saveInfo(Map<String, Info> map, File item, List<String> incFilter, List<String> excFilter) throws IOException {
        String fileName = item.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        if ((incFilter == null || incFilter.contains(ext)) && (excFilter == null || !excFilter.contains(ext))) {

            BufferedReader reader = new BufferedReader(new FileReader(item.getAbsolutePath()));

            Long allRowsCount = 0L;
            Long notEmptyRowsCount = 0L;
            Long commentsCount = 0L;

            while (reader.ready()) {
                allRowsCount++;
                String line = reader.readLine();
                if (!line.isEmpty()) {
                    notEmptyRowsCount++;
                    if (line.indexOf("//") == 0 || line.indexOf('#') == 0) {
                        commentsCount++;
                    }
                }
            }

            // Элемент уже есть в MAP, суммируем данные
            if (map.containsKey(ext)) {
                map.get(ext).addItemInfo(item.length(), allRowsCount, notEmptyRowsCount, commentsCount);
            }
            // Элемента нет в MAP, создаём новый
            else
                map.put(ext, new Info(item.length(), allRowsCount, notEmptyRowsCount, commentsCount));
        }
    }

    private static void parseLine(String str, Params params) {
        // Список параметров
        List<String> optiins = Arrays.asList("--recursive", "--max-depth", "--thread",
                "--include-ext", "--exclude-ext", "--output", "help");

        // Первый параметр всегда help или path. Поскольку в path могут быть пробелы, парсим его отдельно
        // Ищем начало первого параметра
        int ind = str.indexOf("--");
        // Если не нашли, значит либо команда help, либо запуск без параметров и в строке только path
        if (ind < 0) {
            params.setPath(str.trim());
            if (params.getPath().contentEquals("help")) {
                params.setErrorOrHelpMessage("Пример строки запуска: C://test --recursive --max-depth=4 --thread=2 --include-ext=txt,tre --exclude-ext=doc,sql --output=json");
            }
        // Если нашли вхождение - первая часть принадлежит параметру path
        } else {
            params.setPath(str.substring(0, ind).trim());

            // Преобразование строки без параметра path в список строк, разделитель пробел
            List<String> splittedLine = Arrays.asList(str.substring(ind).split("\\s+"));

            if (!splittedLine.isEmpty()) {
                for (String s : splittedLine) {
                    if (params.getErrorOrHelpMessage() != null) {
                        break;
                    }

                    String substr;
                    int index = s.indexOf('=');

                    if (index > 1) {
                        substr = s.substring(0, index);
                    } else {
                        substr = s;
                    }

                    try {
                        if (optiins.contains(substr)) {
                            switch (substr) {
                                case "--recursive":
                                    if (params.getRecursively() == null) {
                                        params.setRecursively(true);
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --recursive");
                                    }
                                    break;
                                case "--max-depth":
                                    if (params.getDepth() == null) {
                                        params.setDepth(Integer.parseInt(s.substring(s.indexOf('=') + 1)));
                                        if (params.getDepth() <= 0) {
                                            params.setErrorOrHelpMessage("Параметр --max-depth должен быть положительным");
                                        }
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --max-depth");
                                    }
                                    break;
                                case "--include-ext":
                                    if (params.getIncludeFilter() == null) {
                                        params.setIncludeFilter(s.substring(s.indexOf('=') + 1));
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --include-ext");
                                    }
                                    break;
                                case "--exclude-ext":
                                    if (params.getExcludeFilter() == null) {
                                        params.setExcludeFilter(s.substring(s.indexOf('=') + 1));
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --exclude-ext");
                                    }
                                    break;
                                case "--output":
                                    if (params.getOutput() == null) {
                                        params.setOutput(s.substring(s.indexOf('=') + 1));
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --output");
                                    }
                                    break;
                                case "--thread":
                                    if (params.getThread() == null) {
                                        params.setThread(Integer.parseInt(s.substring(s.indexOf('=') + 1)));
                                        if (params.getThread() <= 0) {
                                            params.setErrorOrHelpMessage("Параметр --thread должен быть положительным");
                                        }
                                    } else {
                                        params.setErrorOrHelpMessage("Параметр введён больше одного раза: --thread");
                                    }
                                    break;
                            }
                        } else {
                            params.setErrorOrHelpMessage("Параметр не поддерживается: " + s);
                        }
                    } catch (Exception e) {
                        params.setErrorOrHelpMessage("Некорректное значение параметра: " + e);
                    }
                }
            }
        }
    }
}

