import Foundation

@objc public class HciAsr: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
