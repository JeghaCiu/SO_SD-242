import threading
import time

class LabTimerManager:
    def __init__(self):
        self.interval_timer = None
        self.stop_timer = None
        self.is_running = False

#1
    def run_delayed_task(self, delay, callback):
        print(f"1. Задача запланирована через {delay} сек...")
        timer = threading.Timer(delay, callback)
        timer.start()
        return timer

#2
    def _interval_worker(self, period, task_function):
        if self.is_running:
            task_function()
            if self.is_running:  # Проверяем, не остановили ли нас во время выполнения task_function
                self.interval_timer = threading.Timer(period, self._interval_worker, args=(period, task_function))
                self.interval_timer.start()

    def start_periodic_task(self, period, task_function):
        self.is_running = True
        print(f"2. Работа циклического таймера (период: {period} сек)")
        self._interval_worker(period, task_function)

#3
    def start_timed_session(self, period, task_function, duration, on_stop_callback=None):
        print(f"3. Работа сессии запущена на {duration} сек (период: {period} сек...")
        self.start_periodic_task(period, task_function)

        def auto_stop():
            self.stop_all()
            print(f"Время сессии ({duration} сек) истекло. ")
            if on_stop_callback:
                on_stop_callback()

        self.stop_timer = threading.Timer(duration, auto_stop)
        self.stop_timer.start()

    def stop_all(self):
        self.is_running = False
        if self.interval_timer:
            self.interval_timer.cancel()
        if self.stop_timer:
            self.stop_timer.cancel()
        print("Все таймеры остановлены.")


if __name__ == "__main__":
    manager = LabTimerManager()


    def my_task():
        print(f" Действие выполнено в: {time.strftime('%H:%M:%S')}")


    def on_delayed():
        print(" Однократное событие сработало!")


    print("Тестирование таймеров")
    manager.run_delayed_task(delay=2, callback=on_delayed)
    time.sleep(3)

    manager.start_timed_session(period=1.5, task_function=my_task, duration=6)
    time.sleep(7)