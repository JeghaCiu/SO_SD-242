import tkinter as tk
from timer_1 import LabTimerManager


class TimerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Лабораторная работа №1 — Таймеры")
        self.root.geometry("420x360")

        self.manager = LabTimerManager()
        self.counter = 0

        self.status_label = tk.Label(root, text="Статус: ожидание", font=("Arial", 12))
        self.status_label.pack(pady=10)

        self.counter_label = tk.Label(root, text="Циклов выполнено: 0", font=("Arial", 11))
        self.counter_label.pack(pady=5)

        frame1 = tk.LabelFrame(root, text="Режим 1: однократный запуск (delay)")
        frame1.pack(fill="x", padx=10, pady=5)
        tk.Button(frame1, text="Старт (сработает через 3 сек)",
                  command=self.start_delayed).pack(pady=5)

        frame2 = tk.LabelFrame(root, text="Режим 2: циклический запуск (period)")
        frame2.pack(fill="x", padx=10, pady=5)
        tk.Button(frame2, text="Старт (период 1.5 сек)",
                  command=self.start_periodic).pack(pady=5)

        frame3 = tk.LabelFrame(root, text="Режим 3: ограничение по времени")
        frame3.pack(fill="x", padx=10, pady=5)
        tk.Button(frame3, text="Старт (6 сек, период 1.5 сек)",
                  command=self.start_timed).pack(pady=5)

        tk.Button(root, text="СТОП", fg="white", bg="red",
                  command=self.stop_all).pack(pady=15)

    def start_delayed(self):
        self.set_status("Ожидание однократного события...")
        self.manager.run_delayed_task(delay=3, callback=self.on_delayed_done)

    def on_delayed_done(self):
        self.set_status("Однократное событие сработало!")

    def start_periodic(self):
        self.counter = 0
        self.update_counter_label()
        self.set_status("Циклический таймер работает...")
        self.manager.start_periodic_task(period=1.5, task_function=self.on_tick)

    def on_tick(self):
        self.counter += 1
        self.update_counter_label()

    def start_timed(self):
        self.counter = 0
        self.update_counter_label()
        self.set_status("Сессия с ограничением по времени запущена...")
        self.manager.start_timed_session(
            period=1.5,
            task_function=self.on_tick,
            duration=6,
            on_stop_callback=self.on_session_end
        )

    def on_session_end(self):
        self.set_status("Сессия завершена (время истекло)")

    def stop_all(self):
        self.manager.stop_all()
        self.set_status("Остановлено пользователем")

    def set_status(self, text):
        self.root.after(0, lambda: self.status_label.config(text=f"Статус: {text}"))

    def update_counter_label(self):
        count = self.counter
        self.root.after(0, lambda: self.counter_label.config(
            text=f"Циклов выполнено: {count}"))


if __name__ == "__main__":
    root = tk.Tk()
    app = TimerApp(root)
    root.mainloop()