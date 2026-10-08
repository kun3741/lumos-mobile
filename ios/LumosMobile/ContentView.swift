import SwiftUI

private enum LumosColor {
    static let ink = Color(red: 17 / 255, green: 24 / 255, blue: 39 / 255)
    static let night = Color(red: 16 / 255, green: 24 / 255, blue: 39 / 255)
    static let amber = Color(red: 255 / 255, green: 209 / 255, blue: 102 / 255)
    static let canvas = Color(red: 247 / 255, green: 248 / 255, blue: 250 / 255)
}

struct ContentView: View {
    private let screens = [
        "Каталог регіонів",
        "Деталі графіка",
        "Налаштування черги та сповіщень"
    ]

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                hero

                BriefCard(title: "Екрани застосунку") {
                    VStack(alignment: .leading, spacing: 12) {
                        ForEach(Array(screens.enumerated()), id: \.offset) { index, screen in
                            HStack(alignment: .firstTextBaseline, spacing: 16) {
                                Text(String(format: "%02d", index + 1))
                                    .fontWeight(.bold)
                                    .foregroundStyle(Color(red: 183 / 255, green: 121 / 255, blue: 31 / 255))
                                Text(screen)
                                    .foregroundStyle(LumosColor.ink)
                            }
                        }
                    }
                }

                BriefCard(title: "Джерело даних") {
                    Text("Власний сервер Люмос: /api/regions та графіки обленерго для Івано-Франківщини, Львівщини й Волині.")
                        .foregroundStyle(LumosColor.ink)
                }

                BriefCard(title: "На пристрої") {
                    Text("Обраний регіон і черга зберігатимуться локально.")
                        .foregroundStyle(LumosColor.ink)
                }
            }
            .padding(20)
        }
        .background(LumosColor.canvas)
    }

    private var hero: some View {
        VStack(alignment: .leading, spacing: 16) {
            HStack(spacing: 12) {
                Image(systemName: "bolt.fill")
                    .font(.system(size: 30, weight: .bold))
                    .foregroundStyle(LumosColor.amber)
                Text("ЛЮМОС")
                    .font(.system(size: 28, weight: .heavy))
                    .foregroundStyle(.white)
            }

            Text("ПРОЄКТ · ЛАБОРАТОРНА 1")
                .font(.system(size: 12, weight: .bold))
                .foregroundStyle(LumosColor.amber)

            Text("Графіки відключень світла для обраної черги в одному місці.")
                .font(.system(size: 21, weight: .semibold))
                .foregroundStyle(.white)

            HStack(spacing: 8) {
                Circle()
                    .fill(LumosColor.amber)
                    .frame(width: 8, height: 8)
                Text("Івано-Франківщина · Львівщина · Волинь")
                    .font(.system(size: 12))
                    .foregroundStyle(Color(red: 212 / 255, green: 218 / 255, blue: 229 / 255))
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(24)
        .background(LumosColor.night, in: RoundedRectangle(cornerRadius: 28))
    }
}

private struct BriefCard<Content: View>: View {
    let title: String
    let content: Content

    init(title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text(title)
                .font(.system(size: 18, weight: .bold))
                .foregroundStyle(LumosColor.ink)
            content
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(22)
        .background(.white, in: RoundedRectangle(cornerRadius: 22))
    }
}

#Preview {
    ContentView()
}
